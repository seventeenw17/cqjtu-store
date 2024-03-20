package com.backend.springstore.order.service;

import com.backend.springstore.page.PageDTO;
import com.backend.springstore.page.PageVO;
import com.backend.springstore.order.pojo.dto.CreatOrderDTO;
import com.backend.springstore.order.pojo.dto.OrderPaidDTO;
import com.backend.springstore.order.pojo.dto.UpdateOrderAddressDTO;
import com.backend.springstore.order.pojo.vo.OrderManageVO;
import com.backend.springstore.order.pojo.vo.OrderVO;
import com.backend.springstore.user.pojo.vo.UserLoginVO;


import java.util.List;
import java.util.Map;

public interface OrderService {
    List<OrderVO> getList(UserLoginVO userLoginVO);

    void orderPaid(OrderPaidDTO orderPaidDTO, UserLoginVO userLoginVO);

    void takeDelivery(OrderPaidDTO orderPaidDTO, UserLoginVO userLoginVO);

    void orderSaleService(OrderPaidDTO orderPaidDTO, UserLoginVO userLoginVO);

    List<OrderVO> getNoPaidList(UserLoginVO userLoginVO);

    List<OrderVO> getNotArrivedList(UserLoginVO userLoginVO);

    List<OrderVO> getReceivedList(UserLoginVO userLoginVO);

    List<OrderVO> getSaleServiceList(UserLoginVO userLoginVO);

    void creatOrder(CreatOrderDTO creatOrderDTO, UserLoginVO userLoginVO);
    PageVO<List<OrderManageVO>> getOrderList(PageDTO pageDTO, UserLoginVO userLoginVO);

    void deleteOrder(Integer orderId, UserLoginVO userLoginVO);

    List<OrderManageVO> getUserOrderList(String userName, UserLoginVO userLoginVO);

    OrderManageVO getOrderByOrderId(String orderId, UserLoginVO userLoginVO);

    void updateOrderAddress(UpdateOrderAddressDTO updateOrderAddressDTO, UserLoginVO userLoginVO);
    Map<String, Integer> calOrderCategory();
    Integer countOrder();
    Double sumMoney();
}
