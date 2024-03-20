package com.backend.springstore.address.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    // 地址id
    private Integer id;
    // 用户id
    private Integer userId;
    // 收货人姓名
    private String name;
    // 省名称
    private String provinceName;
    // 省代码
    private Integer provinceCode;
    // 市名称
    private String cityName;
    // 市代码
    private Integer cityCode;
    // 区名称
    private String areaName;
    // 区代码
    private Integer areaCode;
    // 邮政编码
    private Integer zip;
    // 详细地址
    private String addressDetail;
    // 手机号
    private String phone;
    // 固话
    private String tel;
    // 地址类型
    private String tag;
    // 是否为默认地址
    private Integer isDefault;
    // 创建人
    private String createdUser;
    // 创建时间
    private Date createdTime;
    // 最后修改人
    private String modifiedUser;
    // 修改时间
    private Date modifiedTime;
}
