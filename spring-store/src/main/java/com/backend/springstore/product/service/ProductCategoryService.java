package com.backend.springstore.product.service;

import com.backend.springstore.product.pojo.vo.ProductCategoryVO;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategoryVO> getProductCategoryList(Integer parentId);
    List<ProductCategoryVO> getAllProductCategoryList();
}
