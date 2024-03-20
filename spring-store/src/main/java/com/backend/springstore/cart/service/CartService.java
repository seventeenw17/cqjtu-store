package com.backend.springstore.cart.service;

import com.backend.springstore.cart.pojo.dto.CartDTO;
import com.backend.springstore.cart.pojo.vo.CartVO;
import com.backend.springstore.user.pojo.vo.UserLoginVO;

import java.util.List;
import java.util.Map;

public interface CartService {
    List<CartVO> getCartList(UserLoginVO userLoginVO);
    void deleteItemById(List<Integer> cartIdList, UserLoginVO userLoginVO);
    void addCartItem(CartDTO cartDTO, UserLoginVO userLoginVO);
    void updateNum(CartDTO cartDTO, UserLoginVO userLoginVO);
    List<Integer> admitBuy(List<CartDTO> cartDTOList, UserLoginVO userLoginVO);
    Integer countCart();
}
