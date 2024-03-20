package com.backend.springstore.product.controller;

import com.backend.springstore.page.PageDTO;
import com.backend.springstore.product.pojo.dto.BackgroundProductDTO;
import com.backend.springstore.product.pojo.dto.CategoryProductDTO;
import com.backend.springstore.product.pojo.dto.ProductDTO;
import com.backend.springstore.product.pojo.vo.BackgroundProductVO;
import com.backend.springstore.security.JWTUtils;
import com.backend.springstore.common.Result;
import com.backend.springstore.page.PageVO;
import com.backend.springstore.product.pojo.dto.ProductCategoryPageDTO;
import com.backend.springstore.product.pojo.vo.ProductDetailVO;
import com.backend.springstore.product.pojo.vo.ProductListVO;
import com.backend.springstore.product.pojo.vo.ProductVO;
import com.backend.springstore.product.service.ProductService;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("product")
@Slf4j
@Api(tags = "商品模块")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("list/new")
    @ApiOperation("获取新商品列表")
    public Result<List<ProductVO>> getNewProductList() {
        List<ProductVO> productVOList = productService.getNewProductList();
        return Result.ok(productVOList);
    }

    @GetMapping("list/hot")
    @ApiOperation("获取热门商品列表")
    public Result<List<ProductVO>> getHotProductList() {
        List<ProductVO> productVOList = productService.getHotProductList();
        return Result.ok(productVOList);
    }

    @GetMapping("detail/{id}")
    @ApiOperation("根据商品ID获取明细")
    public Result<ProductDetailVO> getDetailById(@PathVariable Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        // 已经登录
        UserLoginVO userLoginVO = null;
        if (token != null && !token.isEmpty()) {
            userLoginVO = JWTUtils.getUserInfo(token);
        }
        ProductDetailVO productDetailVO = productService.getDetailById(id, userLoginVO);
        return Result.ok(productDetailVO);
    }

    @GetMapping("list_1")
    @ApiOperation("根据商品分类获取商品列表（自定义分页）")
    public Result<PageVO<List<ProductVO>>> getListByCategoryId(
            @RequestBody ProductCategoryPageDTO categoryPageDTO) {
        PageVO<List<ProductVO>> listPageVO = productService.getProductListByCategoryId(categoryPageDTO);
        return Result.ok(listPageVO);
    }

    @GetMapping("list_2")
    @ApiOperation("根据商品分类获取商品列表")
    public Result<PageVO<List<ProductListVO>>> getListByCategoryId(
            @RequestBody @Validated ProductCategoryPageDTO categoryPageDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = null;
        if (token != null && !token.isEmpty()) {
            // 已经登录
            userLoginVO = JWTUtils.getUserInfo(token);
        }
        PageVO<List<ProductListVO>> pageVO =
                productService.getProductListByCategoryId(categoryPageDTO, userLoginVO);
        return Result.ok(pageVO);
    }


    @PostMapping("getList")
    public Result<PageVO<List<BackgroundProductVO>>> getProductList(@RequestBody PageDTO pageDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        PageVO<List<BackgroundProductVO>> pageVO = productService.getProductList(pageDTO);

        return Result.ok(pageVO);
    }
    @PostMapping("modify")
    public Result<Void> modifyProduct(@RequestBody BackgroundProductDTO backgroundProductDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        productService.modifyProduct(backgroundProductDTO,userLoginVO);
        return Result.ok();
    }
    @PostMapping("getCategoryList")
    public Result<PageVO<List<BackgroundProductVO>>> getCategoryProductList(@RequestBody CategoryProductDTO categoryProductDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        PageVO<List<BackgroundProductVO>> pageVO = productService.getCategoryProductList(categoryProductDTO);

        return Result.ok(pageVO);
    }
    @DeleteMapping("deleteProduct/{id}")
    public  Result<Void> deleteProduct(@PathVariable Integer id, HttpServletRequest request){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        productService.deleteProduct(productDTO);
        return Result.ok();
    }
    @PostMapping("addProduct")
    public Result<Void> addProduct(@RequestBody BackgroundProductDTO backgroundProductDTO, HttpServletRequest request) {


        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        productService.addProduct(backgroundProductDTO,userLoginVO);
        return Result.ok();
    }

    @GetMapping("findIdProduct/{id}")
    public Result<BackgroundProductVO> getProductById(@PathVariable Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        BackgroundProductVO backgroundProductVO = productService.getProductById(id, userLoginVO);
        return Result.ok(backgroundProductVO);

    }


    @GetMapping("calProduct")
    public Result<Map<String, Integer>> getCalProductNum() {
        Map<String, Integer> map = productService.getCalProduct();
        return Result.ok(map);
    }

    @GetMapping("count")
    public Result<Integer> countProduct() {
        Integer cnt = productService.countProduct();
        return Result.ok(cnt);
    }
}
