package com.backend.springstore.product.pojo.entity;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    // 编号
    private Integer id;
    // 父类编号，无则为0
    private Integer parentId;
    // 种类名
    private String name;
    // 状态
    private Integer status;
    // 排序号
    private Integer sortOrder;
    // 是否为父类
    private Integer isParent;
    // 创建人
    private String createdUser;
    // 创建时间
    private Date createdTime;
    // 最近修改人
    private String modifiedUser;
    // 最近修改时间
    private Date modifiedTime;
}
