package com.backend.springstore.product.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品明细
 */
@Data
@ApiModel(value = "返回的商品明细信息", description = "对前端需要的商品信息封装")
public class ProductDetailVO extends ProductVO {
    // 是否被收藏
    @ApiModelProperty(value = "是否被是收藏", name = "isFavorite")
    private Integer isFavorite;
    // 卖点
    @ApiModelProperty(value = "卖点", name = "sellPoint")
    private String sellPoint;
}
