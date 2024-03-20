package com.backend.springstore.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录信息
 */
@Data
public class UserLoginDTO {
    // 用户名
    @NotBlank(message = "登录用户名不能为空")
    @ApiModelProperty(value = "用户名", name = "username", required = true)
    private String username;
    // 密码
    @NotBlank(message = "登录密码不能为空")
    @ApiModelProperty(value = "密码", name = "password", required = true)
    private String password;
}
