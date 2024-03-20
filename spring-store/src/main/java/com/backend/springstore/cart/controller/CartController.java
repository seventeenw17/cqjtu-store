package com.backend.springstore.cart.controller;

import com.backend.springstore.cart.pojo.dto.CartDTO;
import com.backend.springstore.cart.pojo.vo.CartVO;
import com.backend.springstore.cart.service.CartService;
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
import java.util.Map;

@RestController
@RequestMapping("cart")
@Slf4j
@Api(tags = "购物车模块")
@CrossOrigin
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("list")
    @ApiOperation("获取购物车列表接口")
    public Result<List<CartVO>> getCartList(HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        // 获取地址列表
        List<CartVO> cartVOList = cartService.getCartList(userLoginVO);
        return Result.ok(cartVOList);
    }

    @PostMapping("add")
    @ApiOperation("新增购物车条目接口")
    public Result<Void> addCartItem(@RequestBody @Validated CartDTO cartDTO, HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        // 添加购物车商品条目
        cartService.addCartItem(cartDTO, userLoginVO);
        return Result.ok();
    }

    @DeleteMapping("delete")
    @ApiOperation("删除购物车条目接口")
    public Result<Void> deleteItemById(@RequestBody Map<String, Object> cartIdList, HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        cartService.deleteItemById((List<Integer>) cartIdList.get("idList"), userLoginVO);
        return Result.ok();
    }

    @PutMapping("updatenum")
    @ApiOperation("修改商品数量")
    public Result<Void> updateNum(@RequestBody @Validated CartDTO cartDTO, HttpServletRequest request) {
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        cartService.updateNum(cartDTO, userLoginVO);
        return Result.ok();
    }


    @PostMapping("buy")
    @ApiOperation("结算购物车商品接口")
    public Result<List<Integer>> admitBuy(@RequestBody List<CartDTO> CartDTOList, HttpServletRequest request) {
        log.debug("结算信息：{}", CartDTOList);
        log.debug("结算信息：{}", CartDTOList.size());
        // 获取用户token
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        // 结算业务
      List<Integer> cartIdList = cartService.admitBuy(CartDTOList, userLoginVO);
        return Result.ok(cartIdList);
    }

    @GetMapping("count")
    public Result<Integer> countCart() {
        Integer cnt = cartService.countCart();
        return Result.ok(cnt);
    }
}
