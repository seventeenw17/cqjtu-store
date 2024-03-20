package com.backend.springstore.product.pojo.dto;

import com.backend.springstore.page.PageDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 分类查询商品列表分页DTO
 */
@Data
public class ProductCategoryPageDTO extends PageDTO {
    // 分类ID
    @NotNull(message = "分类id不能为空")
    private Integer categoryId;
}