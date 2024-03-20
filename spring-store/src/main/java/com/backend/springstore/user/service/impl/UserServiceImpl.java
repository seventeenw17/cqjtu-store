package com.backend.springstore.user.service.impl;

import com.backend.springstore.common.*;
import com.backend.springstore.security.JWTUtils;
import com.backend.springstore.security.MD5Utils;
import com.backend.springstore.user.mapper.UserMapper;
import com.backend.springstore.user.pojo.dto.*;
import com.backend.springstore.user.pojo.entity.User;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import com.backend.springstore.user.pojo.vo.UserInfoVO;
import com.backend.springstore.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     */
    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        // 用户名是否为空字符串
        if (userRegisterDTO.getUsername().isEmpty()) {
            throw new ServiceException(ServiceCode.ERROR_IS_ASSOCIATED, "用户名不能为空！");
        }
        // 密码是否为空字符串
        if (userRegisterDTO.getPassword().isEmpty()) {
            throw new ServiceException(ServiceCode.ERROR_IS_ASSOCIATED, "密码不能为空！");
        }
        // 确认密码是否为空字符串
        if (userRegisterDTO.getRePassword().isEmpty()) {
            throw new ServiceException(ServiceCode.ERROR_IS_ASSOCIATED, "确认密码不能为空！");
        }
        // 用户名不能和已有的用户一致
        User existedUser = userMapper.getUserByName(userRegisterDTO.getUsername());
        if (existedUser != null) {
            throw new ServiceException(ServiceCode.ERROR_EXISTS, "该用户已存在！");
        }
        // 判断两次密码是否一样
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getRePassword())) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "两次密码不一致！");
        }
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        // 密码加密
        String salt = UUID.randomUUID().toString().replace("-","");
        user.setSalt(salt);
        String password = MD5Utils.encodePassword(userRegisterDTO.getPassword(), salt,Constants.HASH_TIME);
        user.setPassword(password);
        // 没有被删除
        user.setIsDelete(Constants.IS_NOT_DELETED);
        user.setCreatedUser(userRegisterDTO.getUsername());
        int res = userMapper.insert(user);

        if (res != 1) {
            // 添加失败的异常处理
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "注册用户失败！");
        }
    }

    /**
     * 用户登录
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        User existedUser = userMapper.getUserByName(userLoginDTO.getUsername());
        // 用户是否存在
        if (existedUser == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "用户不存在！");
        }
        // 对密码进行加密后判断
        String password = MD5Utils.encodePassword(userLoginDTO.getPassword(), existedUser.getSalt(), Constants.HASH_TIME);
        if (!password.equals(existedUser.getPassword())) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "用户名或密码错误！");
        }
        // 从User取出数据
        // 生成token
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setId(existedUser.getId());
        userLoginVO.setUsername(existedUser.getUsername());

        User user = new User();
        user.setUsername(existedUser.getUsername());
        user.setModifiedUser(existedUser.getUsername());;
        userMapper.login(user);

        String token = JWTUtils.generateToken(userLoginVO);
        userLoginVO.setToken(token);
        return userLoginVO;
    }

    /**
     * 修改密码
     */
    @Override
    public void updatePassword(UserPasswordUpdateDTO userPasswordUpdateDTO, UserLoginVO userLoginVO) {
        User user = userMapper.getUserByName(userLoginVO.getUsername());
        // 用户是否存在
        if (user == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "用户不存在！");
        }
        // 判断原密码
        String oldPassword = MD5Utils.encodePassword(userPasswordUpdateDTO.getOldPassword(), user.getSalt(), Constants.HASH_TIME);
        if (!oldPassword.equals(user.getPassword())) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "原密码错误！");
        }
        // 判断新密码和确认新密码是否一致
        if (!userPasswordUpdateDTO.getNewPassword().equals(userPasswordUpdateDTO.getReNewPassword())) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "两次新密码不一致！");
        }
        // 修改密码
        // 获取盐值
        String salt = UUID.randomUUID().toString().replace("-", "");
        // 加密
        String newPassword = MD5Utils.encodePassword(userPasswordUpdateDTO.getNewPassword(), salt, Constants.HASH_TIME);
        // 修改密码业务
        // 构造一个用户对象
        User updateUser = new User();
        updateUser.setId(userLoginVO.getId());
        updateUser.setPassword(newPassword);
        updateUser.setSalt(salt);
        updateUser.setModifiedUser(userLoginVO.getUsername());
        int res = userMapper.updatePassword(updateUser);
        if (res != 1) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "修改密码失败！");
        }
    }

    /**
     * 获取用户信息
     */
    @Override
    public UserInfoVO getUserInfo(UserLoginVO userLoginVO) {
        User user = userMapper.getUserByName(userLoginVO.getUsername());
        if (user == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"用户不存在！");
        }
        // 封装查询的用户信息
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setPhone(user.getPhone());
        userInfoVO.setEmail(user.getEmail());
        userInfoVO.setGender(user.getGender());
        return userInfoVO;
    }

    /**
     * 修改用户信息
     */
    @Override
    public void updateInfo(UserInfoUpdateDTO userInfoUpdateDTO, UserLoginVO userLoginVO) {
        // 是否修改的是自己的
        if (!userInfoUpdateDTO.getUsername().equals(userLoginVO.getUsername())) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "该用户禁止修改！");
        }
        User user = userMapper.getUserByName(userLoginVO.getUsername());
        // 用户是否存在
        if (user == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "用户不存在！");
        }
        User updateUser = new User();
        updateUser.setUsername(userLoginVO.getUsername());
        updateUser.setPhone(userInfoUpdateDTO.getPhone());
        updateUser.setEmail(userInfoUpdateDTO.getEmail());
        updateUser.setGender(userInfoUpdateDTO.getGender());
        updateUser.setModifiedUser(userLoginVO.getUsername());
        int res = userMapper.updateInfo(updateUser);
        if (res != 1) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "修改用户信息失败！");
        }
    }

    /**
     * 上传头像
     */
    @Override
    public void uploadAvatar(MultipartFile avatar, UserLoginVO userLoginVO) {
        // 图片保存到哪里
        // 暂时保存到源码中：\resources\static\images
        // E:\assets\cqjtu-store\spring-store\src\main\resources\static\images
        String savePath = System.getProperty("user.dir") + File.separator
                + "spring-store" + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "resources" + File.separator
                + "static" + File.separator
                + "images";
        log.debug("文件保存路径：{}", savePath);
        // 获取文件全称
        String fileName = avatar.getOriginalFilename();
        // 判断文件类型是否符合要求
        if (!Constants.IMAGE_FILE_TYPE.contains(fileName.substring(fileName.lastIndexOf(".") + 1))) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "请上传\".jpg\"或\".jpeg\"或\".png\"格式的图片！");
        }
        // 判断文件大小是否符合要求
        if (avatar.getSize() > Constants.MAX_FILE_SIZE) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "图片大小须在2M内！");
        }
        // 防止重复图片生成随机名称
        fileName = UUID.randomUUID().toString().replace("-", "")
                +fileName.substring(fileName.lastIndexOf("."));
        log.debug("文件新名称：{}", fileName);
        // 上传
        File saveFile = new File(savePath, fileName);
        try {
            avatar.transferTo(saveFile);
        } catch (IOException e) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "头像上传失败！");
        }
        // 头像路径持久化到数据库
        User user = new User();
        user.setUsername(userLoginVO.getUsername());
        user.setAvatar(fileName);
        user.setModifiedUser(userLoginVO.getUsername());
        int res = userMapper.saveAvatar(user);
        if (res != 1) {
            // 删除文件
            if (saveFile.exists()) {
                saveFile.delete();
            }
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "头像保存数据库上传失败！");
        }
    }

    @Override
    public Integer countUser() {
        Integer cnt = userMapper.countUser();
        return cnt;
    }

    @Override
    public List<User> selectUser(UserDeleteDTO userDeleteDTO, UserLoginVO userLoginVO){
        List<User> user = userMapper.getUser(userDeleteDTO.getUsername());
        if(user==null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"用户不存在");
        }
        return user;
    }
    @Override
    public void InsertUser(UserInsertDTO userInsertDTO, UserLoginVO userLoginVO){
        User existsUser =  userMapper.getUserByName(userInsertDTO.getUsername());
        if(existsUser!=null){
            throw new ServiceException(ServiceCode.ERROR_EXISTS,"该用户已经存在,不能重复注册.");
        }
        User user=new User();
        user.setUsername(userInsertDTO.getUsername());
        String salt = UUID.randomUUID().toString().replace("-","");
        user.setSalt(salt);
        String password = MD5Utils.encodePassword(userInsertDTO.getPassword(),salt,Constants.HASH_TIME);
        user.setPassword(password);
        user.setIsDelete(0);
        user.setPhone(userInsertDTO.getPhone());
        user.setEmail(userInsertDTO.getEmail());
        user.setGender(userInsertDTO.getGender());
        user.setCreatedUser(userLoginVO.getUsername());
        int result = userMapper.insertUser(user);
        if(result!=1){
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED,"用户增加失败");
        }
    }

    /**
     * 修改用户信息
     * @param userUpdateDTO 修改的信息
     * @param userLoginVO 修改人
     */
    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO, UserLoginVO userLoginVO) {

        User user = new User();
        user.setId(userUpdateDTO.getId());
        user.setUsername(userLoginVO.getUsername());
        user.setPhone(userUpdateDTO.getPhone());
        user.setEmail(userUpdateDTO.getEmail());
        user.setGender(userUpdateDTO.getGender());
        user.setModifiedUser(userLoginVO.getUsername());
        int result = userMapper.updateUser(user);
        if(result!=1){
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED,"用户修改失败");
        }

    }

    @Override
    public void deleteuser(UserDeleteDTO userDeleteDTO,UserLoginVO userLoginVO) {
        User user1 = userMapper.getUserByName(userDeleteDTO.getUsername());
        if(user1==null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"用户不存在");
        }
        User user=new User();
        user.setUsername(userDeleteDTO.getUsername());
        int result=userMapper.deleteUser(user);
        if (result!=1){
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED,"用户删除失败");
        }
    }


    @Override
    public List<User> getUserList(UserLoginVO userLoginVO){
        List<User> userList = userMapper.getList();
        if(userList.isEmpty()){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"当前无用户存在");
        }
        return userList;
    }
}
