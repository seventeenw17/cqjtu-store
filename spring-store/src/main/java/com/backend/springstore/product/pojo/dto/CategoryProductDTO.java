package com.backend.springstore.product.pojo.dto;

import com.backend.springstore.page.PageDTO;
import lombok.Data;

@Data
public class CategoryProductDTO extends PageDTO {
    private Integer categoryId;
}
