package com.backend.springstore.user.controller;

import com.backend.springstore.security.JWTUtils;
import com.backend.springstore.common.Result;
import com.backend.springstore.user.pojo.dto.*;
import com.backend.springstore.user.pojo.entity.User;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import com.backend.springstore.user.pojo.vo.UserInfoVO;
import com.backend.springstore.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("user")
@Slf4j
@Api(tags = "用户模块")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("register")
    @ApiOperation("用户注册接口")
    public Result<Void> register(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {
        log.debug("用户注册信息：{}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.ok();
    }

    @PostMapping("login")
    @ApiOperation("用户登录接口")
    public Result<UserLoginVO> login(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        log.debug("用户登录信息：{}", userLoginDTO);
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.ok(userLoginVO);
    }

    @PutMapping("password/update")
    @ApiOperation("用户修改密码接口")
    public Result<Void> updatePassword(@RequestBody UserPasswordUpdateDTO userPasswordUpdateDTO,
                                       HttpServletRequest request) {
        log.debug("修改密码信息：{}", userPasswordUpdateDTO);
        // 获取用户身份
        String token = request.getHeader("authorization");
        // 解析token
        UserLoginVO curUser = JWTUtils.getUserInfo(token);
        // 谁在修改密码
        userService.updatePassword(userPasswordUpdateDTO, curUser);
        return Result.ok();
    }

    @GetMapping("info")
    @ApiOperation("获取用户信息接口")
    public Result<UserInfoVO> getUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        UserInfoVO userInfoVO = userService.getUserInfo(userLoginVO);
        return Result.ok(userInfoVO);
    }

    @PutMapping("info/update")
    @ApiOperation("修改用户信息接口")
    public Result<Void> updateUser(@RequestBody UserInfoUpdateDTO userInfoUpdateDTO,
                                   HttpServletRequest request) {
        log.debug("修改用户信息：{}", userInfoUpdateDTO);
        // 获取用户身份
        String token = request.getHeader("authorization");
        // 解析token
        UserLoginVO curUser = JWTUtils.getUserInfo(token);
        // 谁在修改用户信息
        userService.updateInfo(userInfoUpdateDTO, curUser);
        return Result.ok();
    }

    @PostMapping("avatar/upload")
    @ApiOperation("上传头像接口")
    public Result<Void> uploadAvatar(MultipartFile avatar, HttpServletRequest request) {
        // 获取用户
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        // 文件上传，将路径保存到数据库
        userService.uploadAvatar(avatar, userLoginVO);
        return Result.ok();
    }

    @PostMapping("logout")
    @ApiOperation("退出登录接口")
    public Result<Void> logout() {
        // 前端处理：用户退出就跳转到登录界面并清空本地存储
        // 后端处理：将token存储在缓存介质中（radis、mcache等），用户退出则清除token
        return Result.ok();
    }

    @GetMapping("count")
    public Result<Integer> countUser() {
        Integer cnt = userService.countUser();
        return Result.ok(cnt);
    }

    @PostMapping ("getlist")
    public Result<List<User>> getUserList(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request){
        log.debug("用户列表:{}",userLoginDTO);
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        List<User> UserList = userService.getUserList(userLoginVO);
        return Result.ok(UserList);
    }

    @PostMapping("delete")
    public Result<Void> deleteUser(@RequestBody UserDeleteDTO userDeleteDTO, HttpServletRequest request){
        log.debug("用户删除:{}",userDeleteDTO);
        //获取修改人
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        userService.deleteuser(userDeleteDTO,userLoginVO);
        return Result.ok();
    }

    @PostMapping("update")
    public Result<Void> updateUser(@RequestBody UserUpdateDTO userUpdateDTO,HttpServletRequest request){
        log.debug("用户修改的信息:{}",userUpdateDTO);
        //获取修改人
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        userService.updateUser(userUpdateDTO,userLoginVO);
        return Result.ok();
    }

    @PostMapping("insert")
    public Result<Void> InsertUser(@RequestBody UserInsertDTO userInsertDTO,HttpServletRequest request){
        log.debug("新增用户:{}",userInsertDTO);
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        userService.InsertUser(userInsertDTO,userLoginVO);
        return Result.ok();
    }

    @PostMapping("select")
    public Result<List<User>> selectUser(@RequestBody UserDeleteDTO userDeleteDTO, HttpServletRequest request){
        log.debug("查询用户:{}",userDeleteDTO);
        //获取用户信息
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        List<User> user= userService.selectUser(userDeleteDTO,userLoginVO);
        return Result.ok(user);
    }
}
