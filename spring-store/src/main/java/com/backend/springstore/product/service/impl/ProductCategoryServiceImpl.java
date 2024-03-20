package com.backend.springstore.product.service.impl;

import com.backend.springstore.product.mapper.ProductCategoryMapper;
import com.backend.springstore.product.pojo.entity.ProductCategory;
import com.backend.springstore.product.pojo.vo.ProductCategoryVO;
import com.backend.springstore.product.service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    /**
     * 获取商品种类列表
     */
    @Override
    public List<ProductCategoryVO> getProductCategoryList(Integer parentId) {
        if (parentId == null) {
            parentId = 0;
        }
        List<ProductCategoryVO> productCategoryVOList = new ArrayList<>();
        List<ProductCategory> productCategoryList = productCategoryMapper.getProductCategoryList(parentId);
        productCategoryList.forEach(productCategory -> {
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            productCategoryVO.setId(productCategory.getId());
            productCategoryVO.setParentId(productCategory.getParentId());
            productCategoryVO.setName(productCategory.getName());
            productCategoryVO.setStatus(productCategory.getStatus());
            productCategoryVOList.add(productCategoryVO);
        });
        return productCategoryVOList;
    }

    /**
     * 获取全部商品种类列表
     */
    @Override
    public List<ProductCategoryVO> getAllProductCategoryList() {
        List<ProductCategoryVO> productCategoryVOList = new ArrayList<>();
        List<ProductCategory> productCategoryList = productCategoryMapper.getAllProductCategoryList();
        productCategoryList.forEach(productCategory -> {
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            productCategoryVO.setId(productCategory.getId());
            productCategoryVO.setParentId(productCategory.getParentId());
            productCategoryVO.setName(productCategory.getName());
            productCategoryVO.setStatus(productCategory.getStatus());
            productCategoryVOList.add(productCategoryVO);
        });
        return productCategoryVOList;
    }
}
