package com.backend.springstore.favorite.service.impl;

import com.backend.springstore.common.ServiceCode;
import com.backend.springstore.common.ServiceException;
import com.backend.springstore.page.PageVO;
import com.backend.springstore.favorite.mapper.FavoriteMapper;
import com.backend.springstore.favorite.pojo.dto.FavoritePageDTO;
import com.backend.springstore.favorite.pojo.entity.Favorite;
import com.backend.springstore.favorite.service.FavoriteService;
import com.backend.springstore.product.mapper.ProductMapper;
import com.backend.springstore.product.pojo.entity.Product;
import com.backend.springstore.product.pojo.vo.ProductListVO;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 获取收藏商品列表
     */
    @Override
    public PageVO<List<ProductListVO>> getFavoriteList(FavoritePageDTO favoritePageDTO, UserLoginVO userLoginVO) {
        if (userLoginVO == null) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "用户未登录！");
        }
        favoritePageDTO.setUserId(userLoginVO.getId());
        List<Product> productList = favoriteMapper.getFavoriteProductByPage(favoritePageDTO);
        if (productList.isEmpty()) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "没有收藏商品！");
        }
        List<ProductListVO> productListVOList = new ArrayList<>();
        productList.forEach(product -> {
            ProductListVO productListVO = new ProductListVO();
            productListVO.setId(product.getId());
            productListVO.setTitle(product.getTitle());
            productListVO.setImage(product.getImage());
            productListVO.setPrice(product.getPrice());
            productListVO.setIsFavorite(1);
            productListVOList.add(productListVO);
        });
        // 商品数量
        int count = favoriteMapper.countByUserId(userLoginVO.getId());
        PageVO<List<ProductListVO>> pageVO = new PageVO<>(
                favoritePageDTO.getPageIndex(),
                favoritePageDTO.getPageSize(),
                count,
                productListVOList);
        return pageVO;
    }

    @Override
    public void addFavorite(Integer productId, UserLoginVO userLoginVO) {
        // 商品编号是否合法
        if (productId == null) {
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST, "商品编号不能为空！");
        }
        Product product = productMapper.getProductById(productId);
        if (product == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "商品编号不存在！");
        }
        // 是否已经被收藏
        Favorite favorite = favoriteMapper.getFavoriteByUserId(productId, userLoginVO.getId());
        if (favorite != null) {
            throw new ServiceException(ServiceCode.ERROR_EXISTS, "该商品已被收藏！");
        }
        Favorite newFavorite = new Favorite();
        newFavorite.setUserId(userLoginVO.getId());
        newFavorite.setProductId(productId);
        newFavorite.setCreatedUser(userLoginVO.getUsername());
        // 添加收藏
        int res = favoriteMapper.addFavorite(newFavorite);
        if (res != 1) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "添加收藏失败！");
        }
    }

    @Override
    public void removeFavorite(Integer productId, UserLoginVO userLoginVO) {
        // 商品编号是否合法
        if (productId == null) {
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST, "商品编号不能为空！");
        }
        // 对商品编号进行强转

        int res = favoriteMapper.deleteFavorite(productId, userLoginVO.getId());
        if (res == 0) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "收藏商品不存在！");
        }
    }

    @Override
    public Integer countFavorite() {
        Integer cnt = favoriteMapper.countFavorite();
        return cnt;
    }
}