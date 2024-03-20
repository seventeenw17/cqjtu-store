package com.backend.springstore.address.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行政地址
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District {
    // 区域ID
    private Integer id;
    // 父级区域编号
    private Integer parent;
    // 区域编号
    private Integer code;
    // 区域名
    private String name;
}
