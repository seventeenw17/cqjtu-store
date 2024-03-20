package com.backend.springstore.product.controller;

import com.backend.springstore.common.Result;
import com.backend.springstore.product.pojo.entity.ProductCategory;
import com.backend.springstore.product.pojo.vo.ProductCategoryVO;
import com.backend.springstore.product.service.ProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("product/category")
@Slf4j
@Api(tags = "商品种类模块")
@CrossOrigin
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("list/all")
    public Result<List<ProductCategoryVO>> getAllProductCategoryList() {
        List<ProductCategoryVO> productCategoryVOList = productCategoryService.getAllProductCategoryList();
        return Result.ok(productCategoryVOList);
    }

    @GetMapping("list")
    @ApiOperation("获取商品种类列表接口")
    public Result<List<ProductCategoryVO>> getProductCategoryList(Integer parentId) {
        List<ProductCategoryVO> productCategoryVOList = productCategoryService.getProductCategoryList(parentId);
        return Result.ok(productCategoryVOList);
    }
}
