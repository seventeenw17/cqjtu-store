package com.backend.springstore.product.mapper;

import com.backend.springstore.product.pojo.dto.CategoryProductDTO;

import com.backend.springstore.page.PageDTO;
import com.backend.springstore.product.pojo.dto.ProductCategoryPageDTO;
import com.backend.springstore.product.pojo.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {
    Product getProductById(Integer productId);
    List<Product> getNewProductList();
    List<Product> getHotProductList();
    int countByCategoryId(Integer categoryId);
    List<Product> getProductByCategoryPage(ProductCategoryPageDTO categoryPageDTO);

    int countAll() ;

    List<Product> getList(PageDTO pageDTO);

    int modifyProduct(Product product);

    List<Product> getCategoryList(CategoryProductDTO categoryProductDTO);

    int countCategory(Integer categoryId);

    int deleteProduct(Integer categoryId);

    int addProduct(Product product);
    List<Product> getAllProduct();
    Integer countProduct();
}
