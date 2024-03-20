package com.backend.springstore.page;

import lombok.Data;

/**
 * 分页VO
 */
@Data
public class PageVO<T> {
    // 页码值
    private Integer pageIndex;
    // 每页显示的数据量
    private Integer pageSize;
    // 总数据量
    private Integer count;
    // 总页码
    private Integer pages;
    // 数据
    private T data;

    public Integer getPages() {
        return count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
    }

    public PageVO(Integer pageIndex, Integer pageSize, Integer count, T data) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.count = count;
        this.data = data;
    }
}