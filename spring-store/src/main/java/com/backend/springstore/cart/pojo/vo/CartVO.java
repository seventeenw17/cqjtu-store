package com.backend.springstore.cart.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

@Data
@ApiModel(value = "返回的购物车信息", description = "对前端需要的购物车信息封装")
public class CartVO {
    // ID
    @ApiModelProperty(value = "ID",  name = "id")
    private Integer id;
    // 用户ID
    @ApiModelProperty(value = "用户ID",  name = "userId")
    private Integer userId;
    // 商品ID
    @ApiModelProperty(value = "商品ID",  name = "productId")
    private Integer productId;
    // 商品标题
    @ApiModelProperty(value = "商品标题",  name = "title")
    private String title;
    // 图片
    @ApiModelProperty(value = "图片",  name = "image")
    private String image;
    // 商品单价
    @ApiModelProperty(value = "商品单价",  name = "price")
    private Double price;
    // 数量
    @ApiModelProperty(value = "数量",  name = "num")
    private Integer num;
    // 状态
    @ApiModelProperty(value = "状态",  name = "status")
    private Integer status;
    // 总价
    @ApiModelProperty(value = "总价",  name = "sumPrice")
    private Double sumPrice;
}
