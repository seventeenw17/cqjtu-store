package com.backend.springstore.order.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateOrderAddressDTO {
    @NotNull(message = "订单编号")
    private Integer orderId;
    @NotNull(message = "地址编号")
    private Integer addressId;
}
