package com.backend.springstore.product.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品列表
 */
@Data
@ApiModel(value = "商品列表VO", description = "对商品列表需要的封装")
public class ProductListVO extends ProductVO {
    @ApiModelProperty(value = "是否被收藏", name = "isFavorite")
    private Integer isFavorite;
}
