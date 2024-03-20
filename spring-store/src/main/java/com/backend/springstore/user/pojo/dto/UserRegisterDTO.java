package com.backend.springstore.user.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "用户注册信息",  description = "对用户注册需要的信息进行封装")
public class UserRegisterDTO {
    // 注册用户名
    @NotBlank(message = "注册用户名不能为空")
    @ApiModelProperty(value = "用户名", name = "username", required = true)
    private String username;
    // 注册密码
    @NotBlank(message = "注册密码不能为空")
    @ApiModelProperty(value = "密码", name = "password", required = true)
    private String password;
    // 注册确认密码
    @NotBlank(message = "注册确认密码不能为空")
    @ApiModelProperty(value = "确认密码", name = "rePassword", required = true)
    private String rePassword;
}
