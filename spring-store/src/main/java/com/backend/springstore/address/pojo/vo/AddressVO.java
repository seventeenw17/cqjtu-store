package com.backend.springstore.address.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 地址列表VO
 */
@Data
@ApiModel(value = "返回的地址信息",  description = "对返回给前端用户的地址信息进行封装")
public class AddressVO {
    // 地址id
    @ApiModelProperty(value = "地址id", name = "id")
    private Integer id;
    // 收货人姓名
    @ApiModelProperty(value = "收货人姓名", name = "name")
    private String name;
    // 详细地址
    @ApiModelProperty(value = "详细地址", name = "addressDetail")
    private String addressDetail;
    // 手机号
    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone;
    // 是否为默认地址
    @ApiModelProperty(value = "是否为默认地址", name = "isDefault")
    private Integer isDefault;
    // 地址类型
    @ApiModelProperty(value = "地址类型", name = "tag")
    private String tag;
}
