package com.backend.springstore.order.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OrderItem {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer num;
    private Integer price;
    private String image;
    private String title;
    private String createdUser;
    private Date createdTime;
    private String modifiedUser;
    private Date modifiedTime;

}
