package com.backend.springstore.order.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: cqjtu-store
 * @Titile: OrderVO
 * @Author: Lucky
 * @Description: 订单VO
 */
@Data
public class OrderVO {
    private Integer id;
    private String recvName;
    private Date orderTime;
    private Integer status;
    /*订单总价*/
    private Double totalPrice;
    /*一个订单下有多个订单项 =>1:N*/
    private List<OrderItemVO> orderItemVOList;
}
