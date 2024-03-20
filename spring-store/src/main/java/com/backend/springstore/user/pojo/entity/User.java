package com.backend.springstore.user.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 用户ID
    private Integer id;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 盐值
    private String salt;
    // 是否删除
    private Integer isDelete;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
    // 性别
    private Integer gender;
    // 头像
    private String avatar;
    // 创建用户
    private String createdUser;
    // 创建时间
    private Date createdTime;
    // 最后修改用户
    private String modifiedUser;
    // 最后修改时间
    private Date modifiedTime;
}
