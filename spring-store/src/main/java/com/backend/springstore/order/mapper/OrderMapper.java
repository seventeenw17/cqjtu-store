package com.backend.springstore.order.mapper;

import com.backend.springstore.page.PageDTO;
import com.backend.springstore.order.pojo.dto.NoPaidOrderDTO;
import com.backend.springstore.order.pojo.entity.Order;
import com.backend.springstore.order.pojo.entity.OrderItem;
import com.backend.springstore.order.pojo.vo.OrderManageVO;
import com.backend.springstore.order.pojo.vo.OrderVO;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    List<OrderVO> getOrderList(Integer userId);

    Order getOrderById(Integer id);

    int updateOrderPaid(Order order);

    int updateOrderTakeDelivery(Order order);

    List<OrderVO> getNoPaidOrderList(NoPaidOrderDTO noPaidOrderDTO);

    int creatOrder(Order order);

    int creatOrderItem(OrderItem orderItem);

    public List<OrderManageVO> getAllOrderList(PageDTO pageDTO);

    Integer countList();

    Integer deleteOrder(Integer orderId);

    Integer deleteOrderItem(Integer orderId);

    List<OrderManageVO> getUserOrderList(Integer id);

    OrderManageVO getOrderBuOrderId(String orderId);

    Integer updateOrderAddress(Order order);
    List<OrderVO> getAllList();
    Integer countOrder();
    List<Double> getPriceList();
}
