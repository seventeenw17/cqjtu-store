package com.backend.springstore.address.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 前端传入的地址信息，新增 | 修改
 */
@Data
public class AddressDTO {
    // ID | 若修改则不为空
    private Integer id;
    // 用户编号
    @ApiModelProperty(value = "用户名", name = "username")
    private Integer userId;
    // 收货人
    @NotEmpty(message = "收货人不能为空")
    private String name;
    // 省/直辖市编号
    @NotNull(message = "省/直辖市编号不能为空")
    private Integer provinceCode;
    // 省/直辖市名称
    @NotEmpty(message = "省/直辖市名称不能为空")
    private String provinceName;
    // 城市编号
    @NotNull(message = "城市编号不能为空")
    private Integer cityCode;
    // 城市名称
    @NotEmpty(message = "城市名称不能为空")
    private String cityName;
    // 区域编号
    @NotNull(message = "区域编号不能为空")
    private Integer areaCode;
    // 区域名称
    @NotEmpty(message = "区域名称不能为空")
    private String areaName;
    // 邮政编码
    private Integer zip;
    // 详细地址
    @NotEmpty(message = "详细地址不能为空")
    private String addressDetail;
    // 手机号
    @NotEmpty(message = "手机号不能为空")
    private String phone;
    // 固话
    private String tel;
    // 地址类型
    private String tag;
}
