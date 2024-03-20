package com.backend.springstore.product.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 返回前端的商品类
 */
@Data
@ApiModel(value = "返回的商品信息", description = "对前端需要的商品信息封装")
public class ProductVO {
    // 编号
    @ApiModelProperty(value = "商品编号", name = "id")
    private Integer id;
    // 标题
    @ApiModelProperty(value = "标题", name = "title")
    private String title;
    // 单价
    @ApiModelProperty(value = "单价", name = "price")
    private Double price;
    // 图片
    @ApiModelProperty(value = "图片", name = "image")
    private String image;
}
