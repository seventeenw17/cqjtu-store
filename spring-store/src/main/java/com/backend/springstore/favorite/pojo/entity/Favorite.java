package com.backend.springstore.favorite.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    // 收藏id
    private Integer id;
    // 用户id
    private Integer userId;
    // 商品id
    private Integer productId;
    // 创建用户
    private String createdUser;
    // 创建时间
    private Date createdTime;
    // 最后修改用户
    private String modifiedUser;
    // 最后修改时间
    private Date modifiedTime;
}
