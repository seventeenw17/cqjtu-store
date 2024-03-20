package com.backend.springstore.cart.mapper;

import com.backend.springstore.cart.pojo.entity.Cart;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    List<Cart> getCartListByUserId(Integer userId);
    Cart getCartById(Integer id);
    int deleteItemById(Integer id);
    int countByUserId(Integer id);
    int addCartItem(Cart cart);
    int updateNum(Cart cart);
    Integer countCart();
}
