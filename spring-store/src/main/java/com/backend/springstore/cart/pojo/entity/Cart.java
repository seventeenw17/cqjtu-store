package com.backend.springstore.cart.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    // ID
    private Integer id;
    // 用户ID
    private Integer userId;
    // 商品ID
    private Integer productId;
    // 商品单价
    private Double price;
    // 数量
    private Integer num;
    // 创建人
    private String createdUser;
    // 创建时间
    private Date createdTime;
    // 最后修改人
    private String modifiedUser;
    // 修改时间
    private Date modifiedTime;
}
