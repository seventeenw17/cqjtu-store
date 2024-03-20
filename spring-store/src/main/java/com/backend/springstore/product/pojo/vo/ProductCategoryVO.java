package com.backend.springstore.product.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "返回的商品类型信息", description = "对前端需要的商品类型信息封装")
public class ProductCategoryVO {
    // 编号
    @ApiModelProperty(value = "类型编号", name = "id")
    private Integer id;
    // 父类编号，无则为0
    @ApiModelProperty(value = "父类编号", name = "parentId")
    private Integer parentId;
    // 种类名
    @ApiModelProperty(value = "种类名", name = "name")
    private String name;
    // 状态
    @ApiModelProperty(value = "状态", name = "status")
    private Integer status;
}
