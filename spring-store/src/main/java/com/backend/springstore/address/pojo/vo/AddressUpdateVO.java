package com.backend.springstore.address.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 后端传回的地址信息
 */
@Data
@ApiModel(value = "返回的更新地址信息", description = "返回给前端的地址信息")
public class AddressUpdateVO {
    // ID | 若修改则不为空
    private Integer id;
    // 用户编号
    @ApiModelProperty(value = "用户名", name = "username")
    private Integer userId;
    // 收货人
    @ApiModelProperty(value = "收货人", name = "name")
    private String name;
    // 省/直辖市编号
    @ApiModelProperty(value = "省/直辖市编号", name = "provinceCode")
    private Integer provinceCode;
    // 省/直辖市名称
    @ApiModelProperty(value = "省/直辖市名称", name = "provinceName")
    private String provinceName;
    // 城市编号
    @ApiModelProperty(value = "城市编号", name = "cityCode")
    private Integer cityCode;
    // 城市名称
    @ApiModelProperty(value = "城市名称", name = "cityName")
    private String cityName;
    // 区域编号
    @ApiModelProperty(value = "区域编号", name = "areaCode")
    private Integer areaCode;
    // 区域名称
    @ApiModelProperty(value = "区域名称", name = "areaName")
    private String areaName;
    // 邮政编码
    @ApiModelProperty(value = "邮政编码", name = "zip")
    private Integer zip;
    // 详细地址
    @ApiModelProperty(value = "详细地址", name = "addressDetail")
    private String addressDetail;
    // 手机号
    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone;
    // 固话
    @ApiModelProperty(value = "固话", name = "tel")
    private String tel;
    // 地址类型
    @ApiModelProperty(value = "地址类型", name = "tag")
    private String tag;
}
