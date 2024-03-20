package com.backend.springstore.page;


import lombok.Data;

/**
 * 分页的参数封装
 */
@Data
public class PageDTO {
    // 每页显示的数据量
    private Integer pageSize;
    // 页码值：从1开始
    private Integer pageIndex;
    // 偏移量
    private Integer offset;
    // limit 开始索引, 查询的条数 ，查询第一页：从0  0, 12
    // 查询第二页： (pageIndex - 1) * pageSize; (2 - 1) * 12
    public Integer getOffset() {
        return (pageIndex - 1) * pageSize;
    }
}