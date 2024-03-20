package com.backend.springstore.cart.service.impl;

import com.backend.springstore.cart.mapper.CartMapper;
import com.backend.springstore.cart.pojo.dto.CartDTO;
import com.backend.springstore.cart.pojo.entity.Cart;
import com.backend.springstore.cart.pojo.vo.CartVO;
import com.backend.springstore.cart.service.CartService;
import com.backend.springstore.common.Constants;
import com.backend.springstore.common.ServiceCode;
import com.backend.springstore.common.ServiceException;
import com.backend.springstore.order.mapper.OrderMapper;
import com.backend.springstore.product.mapper.ProductMapper;
import com.backend.springstore.product.pojo.entity.Product;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 获取购物车列表
     */
    @Override
    public List<CartVO> getCartList(UserLoginVO userLoginVO) {
        // 获取列表
        List<Cart> cartList = cartMapper.getCartListByUserId(userLoginVO.getId());
        // 暂无商品
        if (cartList.isEmpty()) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "购物车里暂无商品！");
        }
        List<CartVO> cartVOList = new ArrayList<>();
        cartList.forEach(cart -> {
            CartVO cartVO = new CartVO();
            cartVO.setId(cart.getId());
            cartVO.setProductId(cart.getProductId());
            Product product = productMapper.getProductById(cart.getProductId());
            cartVO.setTitle(product.getTitle());
            cartVO.setImage(product.getImage());
            cartVO.setPrice(cart.getPrice());
            cartVO.setNum(cart.getNum());
            cartVO.setStatus(0);
            cartVO.setSumPrice(cartVO.getPrice() * cartVO.getNum());
            cartVOList.add(cartVO);
        });
        log.debug("获取购物车信息：{}", cartVOList);
        return cartVOList;
    }

    /**
     * 新增购物车商品条目
     */
    @Override
    public void addCartItem(CartDTO cartDTO, UserLoginVO userLoginVO) {
        // 判断用户权限
        if (!cartDTO.getUserId().equals(userLoginVO.getId())) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "无权限操作！");
        }
        // 每个用户购物车数据最多50条
        int count = cartMapper.countByUserId(userLoginVO.getId());
        if (count == Constants.MAX_CART_ITEMS_COUNT) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED,
                    "新增购物车商品失败，每个用户购物车条目最多为" + Constants.MAX_CART_ITEMS_COUNT+ "条！");
        }
        // 封装
        Cart cart = new Cart();
        cart.setUserId(userLoginVO.getId());
        cart.setProductId(cartDTO.getProductId());
        cart.setNum(cartDTO.getNum());
        cart.setPrice(productMapper.getProductById(cartDTO.getProductId()).getPrice());
        cart.setCreatedUser(userLoginVO.getUsername());
        int res = cartMapper.addCartItem(cart);
        if (res != 1) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "添加购物车失败！");
        }
    }

    /**
     * 删除购物车中的商品条目
     */
    @Override
    public void deleteItemById(List<Integer> cartIdList, UserLoginVO userLoginVO) {
        for (Integer id: cartIdList) {
            Cart cart = cartMapper.getCartById(id);
            // 是否存在
            if (cart == null) {
                throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "购物车内不存在该商品！");
            }
            // 是否属于该用户
            if (!cart.getUserId().equals(userLoginVO.getId())) {
                throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "" + id +"对应的条目无操作权限！");
            }
            int res = cartMapper.deleteItemById(id);
            if (res != 1) {
                throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "删除" + id +"对应的条目失败！");
            }
        }
    }

    /**
     * 修改购物车内商品数量
     */
    @Override
    public void updateNum(CartDTO cartDTO, UserLoginVO userLoginVO) {
        Integer id = cartDTO.getId();
        // ID是否为空
        if (id == null) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "ID不能为空！");
        }
        // 根据ID查询条目
        Cart cart = cartMapper.getCartById(id);
        if (cart == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "商品条目不存在！");
        }
        // 是否属于该用户
        if (!cart.getUserId().equals(userLoginVO.getId())) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "无操作权限！");
        }
        // 是否对对应一致
        if (!Objects.equals(cartDTO.getProductId(), cart.getProductId())) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "条目商品对应错误！");
        }
        cart.setNum(cartDTO.getNum());
        cart.setModifiedUser(userLoginVO.getUsername());
        int res = cartMapper.updateNum(cart);
        if (res != 1) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "修改数量失败！");
        }
    }

    /**
     * 结算购物车商品
     */
    @Override
    public List<Integer> admitBuy(List<CartDTO> cartDTOList, UserLoginVO userLoginVO) {
        List<Integer> cartIdList = new ArrayList<>();
        for (CartDTO cartDTO: cartDTOList) {
            cartIdList.add(cartDTO.getId());
            Product product = productMapper.getProductById(cartDTO.getProductId());
            if (cartDTO.getNum() > product.getNum()) {
                throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "商品“" + product.getTitle() + "”库存不足，无法结算！");
            }
        }
        return cartIdList;
    }

    @Override
    public Integer countCart() {
        Integer cnt = cartMapper.countCart();
        return cnt;
    }
}
