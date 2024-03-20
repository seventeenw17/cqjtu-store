package com.backend.springstore.user.pojo.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户修改密码
 */
@Data
public class UserPasswordUpdateDTO {
    // 原密码
    @NotBlank(message = "原密码不能为空")
    @ApiModelProperty(value = "用户名", name = "oldPassword", required = true)
    private String oldPassword;
    // 新密码
    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "用户名", name = "newPassword", required = true)
    private String newPassword;
    // 确认新密码
    @NotBlank(message = "确认新密码不能为空")
    @ApiModelProperty(value = "用户名", name = "reNewPassword", required = true)
    private String reNewPassword;
}
