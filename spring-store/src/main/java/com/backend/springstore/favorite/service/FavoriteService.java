package com.backend.springstore.favorite.service;

import com.backend.springstore.page.PageVO;
import com.backend.springstore.favorite.pojo.dto.FavoritePageDTO;
import com.backend.springstore.product.pojo.vo.ProductListVO;
import com.backend.springstore.user.pojo.vo.UserLoginVO;

import java.util.List;

public interface FavoriteService {
    PageVO<List<ProductListVO>> getFavoriteList(FavoritePageDTO favoritePageDTO, UserLoginVO userLoginVO);
    void addFavorite(Integer productId, UserLoginVO userLoginVO);
    void removeFavorite(Integer productId, UserLoginVO userLoginVO);
    Integer countFavorite();
}
