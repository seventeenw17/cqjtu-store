package com.backend.springstore.cart.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "购物车数据DTO", description = "购物车需要的实体封装")
public class CartDTO {
    // ID
    @ApiModelProperty(value = "ID", name = "id")
    private Integer id;
    // 用户ID
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    private Integer userId;
    // 商品ID
    @ApiModelProperty(value = "商品ID", name = "productId", required = true)
    private Integer productId;
    // 数量
    @ApiModelProperty(value = "数量", name = "num", required = true)
    private Integer num;
}
