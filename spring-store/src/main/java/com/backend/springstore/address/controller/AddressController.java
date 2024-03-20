package com.backend.springstore.address.controller;

import com.backend.springstore.address.pojo.dto.AddressDTO;
import com.backend.springstore.address.pojo.vo.AddressUpdateVO;
import com.backend.springstore.address.pojo.vo.AddressVO;
import com.backend.springstore.address.service.AddressService;
import com.backend.springstore.security.JWTUtils;
import com.backend.springstore.common.Result;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("address")
@Slf4j
@Api(tags = "地址模块")
@CrossOrigin
public class AddressController {
    @Autowired
    AddressService addressService;
    @GetMapping("list")
    @ApiOperation("获取地址列表接口")
    public Result<List<AddressVO>> getAddressList(HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        // 查询地址列表
        List<AddressVO> addressVOList = addressService.getAddressList(userLoginVO);
        return Result.ok(addressVOList);
    }

    @DeleteMapping ("delete/{id}")
    @ApiOperation("删除地址接口")
    public Result<Void> deleteAddress(@PathVariable Integer id, HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        addressService.deleteAddress(id, userLoginVO);
        return Result.ok();
    }

    @PutMapping("default/set/{id}")
    @ApiOperation("设置默认地址接口")
    public Result<Void> setDefaultAddress(@PathVariable Integer id, HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        addressService.setDefaultAddress(id, userLoginVO);
        return Result.ok();
    }

    @PostMapping("add")
    @ApiOperation("新增收货地址接口")
    public Result<Void> addAddress(@RequestBody @Validated AddressDTO addressDTO,
                                    HttpServletRequest request) {
        log.debug("新增收货地址信息：{}", addressDTO);
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        addressService.addAddress(addressDTO, userLoginVO);
        return Result.ok();
    }

    @GetMapping("get/{id}")
    @ApiOperation("获取收货地址接口")
    public Result<AddressUpdateVO> getAddressById(@PathVariable Integer id, HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        AddressUpdateVO addressUpdateVO = addressService.getAddressById(id, userLoginVO);
        return Result.ok(addressUpdateVO);
    }

    @PutMapping("update")
    @ApiOperation("修改收货地址接口")
    public Result<Void> updateAddress(@RequestBody @Validated AddressDTO addressDTO,
                                      HttpServletRequest request) {
        log.debug("修改收货地址信息：{}", addressDTO);
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        addressService.updateAddress(addressDTO, userLoginVO);
        return Result.ok();
    }
    @GetMapping("getList/{userId}")
    @ApiOperation("管理员获取地址列表接口")
    public Result<List<AddressVO>> getAddressList(@PathVariable Integer userId, HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        // 查询地址列表
        List<AddressVO> addressList = addressService.managerGetAddressList(userId,userLoginVO);
        return Result.ok(addressList);
    }
}
