package com.backend.springstore.order.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderManageVO {
    //用户id
    private Integer userId;
    //用户名
    private String userName;
    //订单号
    private Integer id;
    //收件人
    private String recvName;
    //收获省
    private String recvProvince;
    //收获市
    private String recvCity;
    //收货区
    private String recvArea;
    //详细地址
    private String recvAddress;
    //订单创建时间
    private Date orderTime;
    //订单状态
    private Integer status;
    /*订单总价*/
    private Integer totalPrice;
}
