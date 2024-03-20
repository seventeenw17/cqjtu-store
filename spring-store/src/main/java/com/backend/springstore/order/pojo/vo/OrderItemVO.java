package com.backend.springstore.order.pojo.vo;

import lombok.Data;

/**
 * @ProjectName: cqjtu-store
 * @Titile: OrderItemVO
 * @Author: Lucky
 * @Description: 订单项VO
 */
@Data
public class OrderItemVO {
    private Integer id;
    private Integer orderId;
    private String image;
    private Integer productId;
    private Double price;
    private Integer num;
    /*订单项总价*/
    private Integer total;
    private String title;

    public Double getTotal() {
        return price * num;
    }
}
