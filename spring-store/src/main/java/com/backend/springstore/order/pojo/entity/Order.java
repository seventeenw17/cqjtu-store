package com.backend.springstore.order.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ProjectName: cqjtu-store
 * @Titile: Order
 * @Author: Lucky
 * @Description: 订单实体
 */
@Data
public class Order {
    private Integer id;
    private Integer userId;
    private String recvName;
    private String recvPhone;
    private String recvProvince;
    private String recvCity;
    private String recvArea;
    private String recvAddress;
    private String zip;
    private Integer status;
    private Long totalPrice;
    private Date orderTime;
    private Date payTime;
    private Integer isDelete;
    private String createdUser;
    private Date createdTime;
    private String modifiedUser;
    private Date modifiedTime;
}
