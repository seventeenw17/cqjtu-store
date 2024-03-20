package com.backend.springstore.user.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录成功后返回给前端的数据
 */

@Data
@ApiModel(value = "返回用户登录信息",  description = "对返回给前端用户登录信息进行封装")
public class UserLoginVO {
    // 用户id
    @ApiModelProperty(value = "ID", name = "id")
    private Integer id;
    // 用户名
    @ApiModelProperty(value = "用户名", name = "username")
    private String username;
    // token
    @ApiModelProperty(value = "token", name = "token")
    private String token;
}
