package com.backend.springstore.user.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 封装修改用户信息所需要的信息
 */
@Data
@ApiModel(value = "用户修改信息",  description = "对修改信息需要的信息进行封装")
public class UserInfoUpdateDTO {
    // 用户名
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", name = "username", required = true)
    private String username;
    // 修改电话
    @ApiModelProperty(value = "电话", name = "phone", required = true)
    private String phone;
    // 修改邮箱
    @ApiModelProperty(value = "邮箱", name = "email", required = true)
    private String email;
    // 修改性别
    @ApiModelProperty(value = "性别", name = "gender", required = true, dataType = "Integer Between 1 to 2")
    private Integer gender;
}
