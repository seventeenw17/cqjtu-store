package com.backend.springstore.favorite.mapper;

import com.backend.springstore.favorite.pojo.dto.FavoritePageDTO;
import com.backend.springstore.favorite.pojo.entity.Favorite;
import com.backend.springstore.product.pojo.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteMapper {
    Favorite getFavoriteByUserId(@Param("productId") Integer productId, @Param("userId") Integer userId);
    List<Integer> getFavoriteProductIdListByUserId(Integer userId);
    int countByUserId(Integer userId);
    List<Product> getFavoriteProductByPage(FavoritePageDTO pageDTO);
    int addFavorite(Favorite favorite);
    int deleteFavorite(@Param("productId") Integer productId, @Param("userId") Integer userId);
    Integer countFavorite();
}
