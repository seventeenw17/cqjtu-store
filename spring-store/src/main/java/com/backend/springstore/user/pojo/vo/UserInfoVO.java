package com.backend.springstore.user.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "返回用户信息",  description = "对返回给前端用户信息进行封装")
public class UserInfoVO {
    // 用户名
    @ApiModelProperty(value = "用户名", name = "username")
    private String username;
    // 电话
    @ApiModelProperty(value = "电话", name = "username")
    private String phone;
    // 邮箱
    @ApiModelProperty(value = "邮箱", name = "username")
    private String email;
    // 性别
    @ApiModelProperty(value = "性别", name = "username")
    private Integer gender;
}
