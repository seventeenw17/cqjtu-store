package com.backend.springstore.order.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreatOrderItemDTO {
    @NotNull(message = "产品不能为空")
    private Integer productId;
    @NotNull(message = "产品数量不能为空")
    private Integer num;

}
