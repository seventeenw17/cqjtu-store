package com.backend.springstore.product.mapper;

import com.backend.springstore.product.pojo.entity.ProductCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryMapper {
    ProductCategory getCategoryById(Integer id);
    List<ProductCategory> getProductCategoryList(Integer parentId);
    List<ProductCategory> getAllProductCategoryList();
   Integer getParentIdInteger(Integer id);
}
