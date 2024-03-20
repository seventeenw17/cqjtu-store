package com.backend.springstore.favorite.controller;

import com.backend.springstore.security.JWTUtils;
import com.backend.springstore.common.Result;
import com.backend.springstore.page.PageVO;
import com.backend.springstore.favorite.pojo.dto.FavoritePageDTO;
import com.backend.springstore.favorite.service.FavoriteService;
import com.backend.springstore.product.pojo.vo.ProductListVO;
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
@RequestMapping("favorite")
@Slf4j
@Api(tags = "收藏模块")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("list")
    @ApiOperation("获取收藏列表")
    public Result<PageVO<List<ProductListVO>>> getFavoriteList(
            @RequestBody @Validated FavoritePageDTO favoritePageDTO, HttpServletRequest request) {
        // 获取用户
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        PageVO<List<ProductListVO>> pageVO =
                favoriteService.getFavoriteList(favoritePageDTO, userLoginVO);
        return Result.ok(pageVO);
    }

    @PostMapping("add")
    @ApiOperation("新增收藏商品")
    public Result<Void> addFavorite(Integer productId, HttpServletRequest request) {
        // 获取用户
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        // 添加收藏商品
        favoriteService.addFavorite(productId, userLoginVO);
        return Result.ok();
    }

    @DeleteMapping("remove")
    @ApiOperation("取消收藏商品")
    public Result<Void> removeFavorite(Integer productId, HttpServletRequest request) {
        // 获取用户
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        // 删除收藏商品
        favoriteService.removeFavorite(productId, userLoginVO);
        return Result.ok();
    }

    @GetMapping("count")
    public Result<Integer> countFavorite() {
        Integer cnt = favoriteService.countFavorite();
        return Result.ok(cnt);
    }
}
