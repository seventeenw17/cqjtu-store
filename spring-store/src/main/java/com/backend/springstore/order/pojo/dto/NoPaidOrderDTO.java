package com.backend.springstore.order.pojo.dto;

import lombok.Data;

@Data
public class NoPaidOrderDTO {
    private Integer UserId;
    private Integer status;
}
