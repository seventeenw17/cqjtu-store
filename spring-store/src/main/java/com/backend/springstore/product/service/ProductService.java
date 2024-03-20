package com.backend.springstore.product.service;

import com.backend.springstore.product.pojo.dto.BackgroundProductDTO;
import com.backend.springstore.product.pojo.dto.CategoryProductDTO;
import com.backend.springstore.product.pojo.dto.ProductDTO;
import com.backend.springstore.product.pojo.vo.BackgroundProductVO;
import com.backend.springstore.page.PageDTO;
import com.backend.springstore.page.PageVO;
import com.backend.springstore.product.pojo.dto.ProductCategoryPageDTO;
import com.backend.springstore.product.pojo.vo.ProductDetailVO;
import com.backend.springstore.product.pojo.vo.ProductListVO;
import com.backend.springstore.product.pojo.vo.ProductVO;
import com.backend.springstore.user.pojo.vo.UserLoginVO;

import java.util.List;
import java.util.Map;


public interface ProductService {
    List<ProductVO> getNewProductList();
    List<ProductVO> getHotProductList();
    PageVO<List<ProductVO>> getProductListByCategoryId(ProductCategoryPageDTO categoryPageDTO);
    PageVO<List<ProductListVO>> getProductListByCategoryId(ProductCategoryPageDTO categoryPageDTO, UserLoginVO userLoginVO);
    ProductDetailVO getDetailById(Integer id, UserLoginVO userLoginVO);

    PageVO<List<BackgroundProductVO>> getProductList(PageDTO pageDTO);

    void modifyProduct(BackgroundProductDTO backgroundProductDTO, UserLoginVO userLoginVO);

    PageVO<List<BackgroundProductVO>> getCategoryProductList(CategoryProductDTO categoryProductDTO);

    void deleteProduct(ProductDTO productDTO);

    void addProduct(BackgroundProductDTO backgroundProductDTO,UserLoginVO userLoginVO);

    BackgroundProductVO getProductById(Integer id, UserLoginVO userLoginVO);
    Map<String, Integer> getCalProduct();
    Integer countProduct();
}
