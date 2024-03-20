package com.backend.springstore.address.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "返回的行政区域信息",  description = "对返回给前端用户的行政区域信息进行封装")
public class DistrictVO {
    // 区域编号
    @ApiModelProperty(value = "区域编号", name = "code")
    private Integer code;
    // 区域名
    @ApiModelProperty(value = "区域名称", name = "name")
    private String name;
}
