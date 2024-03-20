package com.backend.springstore.product.pojo.dto;

import lombok.Data;

import java.util.Date;
@Data
public class BackgroundProductDTO {
    // 商品ID
    private Integer id;
    // 用户ID
    private Integer categoryId;
    // 商品系列
    private String itemType;
    // 商品标题
    private String title;
    // 商品卖点
    private String sellPoint;
    // 单价
    private Double price;
    // 数量
    private Integer num;
    // 图片
    private String image;
    // 状态
    private Integer status;
    // 显示优先级
    private Integer priority;

}
