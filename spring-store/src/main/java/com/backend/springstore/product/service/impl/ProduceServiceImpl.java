package com.backend.springstore.product.service.impl;

import com.backend.springstore.page.PageDTO;
import com.backend.springstore.product.pojo.dto.BackgroundProductDTO;
import com.backend.springstore.product.pojo.dto.CategoryProductDTO;
import com.backend.springstore.product.pojo.dto.ProductDTO;

import com.backend.springstore.product.pojo.vo.BackgroundProductVO;
import com.backend.springstore.common.ServiceCode;
import com.backend.springstore.common.ServiceException;
import com.backend.springstore.page.PageVO;
import com.backend.springstore.favorite.mapper.FavoriteMapper;
import com.backend.springstore.favorite.pojo.entity.Favorite;
import com.backend.springstore.product.mapper.ProductCategoryMapper;
import com.backend.springstore.product.mapper.ProductMapper;
import com.backend.springstore.product.pojo.dto.ProductCategoryPageDTO;
import com.backend.springstore.product.pojo.entity.Product;
import com.backend.springstore.product.pojo.entity.ProductCategory;
import com.backend.springstore.product.pojo.vo.ProductDetailVO;
import com.backend.springstore.product.pojo.vo.ProductListVO;
import com.backend.springstore.product.pojo.vo.ProductVO;
import com.backend.springstore.product.service.ProductService;
import com.backend.springstore.user.mapper.UserMapper;
import com.backend.springstore.user.pojo.entity.User;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ProduceServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取新商品列表
     */
    @Override
    public List<ProductVO> getNewProductList() {
        List<Product> productList = productMapper.getNewProductList();
        List<ProductVO> productVOList = toConvert(productList);
        log.debug("新商品信息：{}", productVOList);
        return productVOList;
    }

    /**
     * 获取热门商品列表
     */
    @Override
    public List<ProductVO> getHotProductList() {
        List<Product> productList = productMapper.getHotProductList();
        List<ProductVO> productVOList = toConvert(productList);
        log.debug("热门商品信息：{}", productVOList);
        return productVOList;
    }

    /**
     * 封装List<Product>到List<ProductVO>的转化
     */
    private List<ProductVO> toConvert(List<Product> productList){
        List<ProductVO> productVOList = new ArrayList<>();
        productList.forEach(product -> {
            ProductVO productVO = new ProductVO();
            productVO.setId(product.getId());
            productVO.setTitle(product.getTitle());
            productVO.setPrice(product.getPrice());
            productVO.setImage(product.getImage());
            productVOList.add(productVO);
        });
        return productVOList;
    }

    /**
     * 获取商品明细
     */
    @Override
    public ProductDetailVO getDetailById(Integer id, UserLoginVO userLoginVO) {
        //判断这个id是否合法
        Product product =  productMapper.getProductById(id);
        if (product == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "没有此商品！");
        }
        Favorite favorite = null;
        if (userLoginVO != null) {
            // 登录过，是否被当前用户收藏了
            Integer userId = userLoginVO.getId();
            favorite = favoriteMapper.getFavoriteByUserId(id, userId);
        }
        //封装vo
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setTitle(product.getTitle());
        productDetailVO.setImage(product.getImage());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setSellPoint(product.getSellPoint());
        if (favorite != null) {
            productDetailVO.setIsFavorite(1);
        }
        return productDetailVO;
    }

    /**
     * 根据分类ID查询商品列表并分页
     */
    @Override
    public PageVO<List<ProductVO>> getProductListByCategoryId(ProductCategoryPageDTO categoryPageDTO) {
        // 判断该商品分类id是否存在
        ProductCategory productCategory = productCategoryMapper.getCategoryById(categoryPageDTO.getCategoryId());
        if (productCategory == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"该商品分类不存在");
        }
        // 调用持久层方法
        List<Product> productList = productMapper.getProductByCategoryPage(categoryPageDTO);
        if(productList == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"该商品分类没有商品");
        }
        List<ProductVO> productVOList = toConvert(productList);
        int count = productMapper.countByCategoryId(categoryPageDTO.getCategoryId());
        PageVO<List<ProductVO>> pageVO = new PageVO<>(
                categoryPageDTO.getPageIndex(),
                categoryPageDTO.getPageSize(),
                count,productVOList);
        return pageVO;
    }

    /**
     * 根据分类ID查询商品列表并分页-2
     */
    @Override
    public PageVO<List<ProductListVO>> getProductListByCategoryId(ProductCategoryPageDTO categoryPageDTO, UserLoginVO userLoginVO) {
        // 判断该商品分类id是否存在
        ProductCategory productCategory = productCategoryMapper.getCategoryById(categoryPageDTO.getCategoryId());
        if (productCategory == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "该商品分类不存在！");
        }
        // SQL语句进行分页的
        List<Product> productList = productMapper.getProductByCategoryPage(categoryPageDTO);
        if (productList == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "该商品分类没有商品！");
        }
        // 封装成列表vo（分页中的data）
        List<ProductListVO> productListVOList = new ArrayList<>();
        productList.forEach(product -> {
            ProductListVO productListVO = new ProductListVO();
            productListVO.setId(product.getId());
            productListVO.setTitle(product.getTitle());
            productListVO.setImage(product.getImage());
            productListVO.setPrice(product.getPrice());
            // 判断这系列的商品是否被收藏
            if (userLoginVO != null) {
                // 登录了
                Favorite favorite = favoriteMapper.getFavoriteByUserId(product.getId(), userLoginVO.getId());
                if (favorite != null) {
                    // 设置收藏
                    productListVO.setIsFavorite(1);
                }
            }
            productListVOList.add(productListVO);
        });
        // 商品数量
        int count = productMapper.countByCategoryId(productCategory.getId());
        PageVO<List<ProductListVO>> pageVO = new PageVO<>(
                categoryPageDTO.getPageIndex(),
                categoryPageDTO.getPageSize(), count, productListVOList);
        return pageVO;
    }

    @Override
    public PageVO<List<BackgroundProductVO>> getProductList(PageDTO pageDTO) {
        if (pageDTO.getPageIndex() < 1){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"不能小于第1页");
        }
        if (pageDTO.getPageSize() <0){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"每页不能展示小于0个商品");
        }
        List<Product> productList = productMapper.getList(pageDTO);
        if (productList == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"没商品");
        }

        List<BackgroundProductVO> backgroundProductVOList = new ArrayList<>();
        productList.forEach(product->{
            BackgroundProductVO backgroundProductVO = new BackgroundProductVO();
            backgroundProductVO.setId(product.getId());
            backgroundProductVO.setCategoryId(product.getCategoryId());
            backgroundProductVO.setItemType(product.getItemType());
            backgroundProductVO.setTitle(product.getTitle());
            backgroundProductVO.setSellPoint(product.getSellPoint());
            backgroundProductVO.setPrice(product.getPrice());
            backgroundProductVO.setNum(product.getNum());
            backgroundProductVO.setImage(product.getImage());
            if(product.getStatus()==1)
            backgroundProductVO.setStatus(true);
            else
                backgroundProductVO.setStatus(false);
            backgroundProductVO.setPriority(product.getPriority());
            backgroundProductVO.setCreatedUser(product.getCreatedUser());
            backgroundProductVO.setCreatedTime(product.getCreatedTime());
            backgroundProductVO.setModifiedUser(product.getModifiedUser());
            backgroundProductVO.setModifiedTime(product.getModifiedTime());
            backgroundProductVOList.add(backgroundProductVO);
        });

        int count = productMapper.countAll();
        PageVO<List<BackgroundProductVO>> pageVO = new PageVO<>(
                pageDTO.getPageIndex(),
                pageDTO.getPageSize(),
                count,backgroundProductVOList);

        return pageVO;
    }

    @Override
    public void modifyProduct(BackgroundProductDTO backgroundProductDTO, UserLoginVO userLoginVO) {
        User user = userMapper.getUserByName(userLoginVO.getUsername());
        if (user == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此用户不存在");
        }
        System.out.println(520);
        Product product = productMapper.getProductById(backgroundProductDTO.getId());
        if (product == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此商品不存在");
        }
        product.setCategoryId(backgroundProductDTO.getCategoryId());
        product.setItemType(backgroundProductDTO.getItemType());
        product.setTitle(backgroundProductDTO.getTitle());
        product.setSellPoint(backgroundProductDTO.getSellPoint());
        product.setPrice(backgroundProductDTO.getPrice());
        product.setNum(backgroundProductDTO.getNum());
        product.setImage(backgroundProductDTO.getImage());
        product.setStatus(backgroundProductDTO.getStatus());
        product.setPriority(product.getPriority());
        product.setModifiedUser(userLoginVO.getUsername());


        int count = productMapper.modifyProduct(product);
        if (count != 1){
            throw new ServiceException(ServiceCode.ERROR_DELETE_FAILED,"修改产品失败");
        }
    }

    @Override
    public PageVO<List<BackgroundProductVO>> getCategoryProductList(CategoryProductDTO categoryProductDTO) {
        if (categoryProductDTO.getPageIndex() < 1){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"不能小于第1页");
        }
        if (categoryProductDTO.getPageSize() <0){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"每页不能展示小于0个商品");
        }
        List<Product> productList = productMapper.getCategoryList(categoryProductDTO);
        if (productList == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"没此类商品");
        }

        List<BackgroundProductVO> backgroundProductVOList = new ArrayList<>();
        productList.forEach(product->{
            BackgroundProductVO backgroundProductVO = new BackgroundProductVO();
            backgroundProductVO.setId(product.getId());
            backgroundProductVO.setCategoryId(product.getCategoryId());
            backgroundProductVO.setItemType(product.getItemType());
            backgroundProductVO.setTitle(product.getTitle());
            backgroundProductVO.setSellPoint(product.getSellPoint());
            backgroundProductVO.setPrice(product.getPrice());
            backgroundProductVO.setNum(product.getNum());
            backgroundProductVO.setImage(product.getImage());
            if(product.getStatus()==1)
                backgroundProductVO.setStatus(true);
            else
                backgroundProductVO.setStatus(false);
            backgroundProductVO.setPriority(product.getPriority());
            backgroundProductVO.setCreatedUser(product.getCreatedUser());
            backgroundProductVO.setCreatedTime(product.getCreatedTime());
            backgroundProductVO.setModifiedUser(product.getModifiedUser());
            backgroundProductVO.setModifiedTime(product.getModifiedTime());
            backgroundProductVOList.add(backgroundProductVO);
        });

        int count = productMapper.countCategory(categoryProductDTO.getCategoryId());
        PageVO<List<BackgroundProductVO>> pageVO = new PageVO<>(
                categoryProductDTO.getPageIndex(),
                categoryProductDTO.getPageSize(),
                count,backgroundProductVOList);

        return pageVO;
    }

    @Override
    public void deleteProduct(ProductDTO productDTO) {
        Product product = productMapper.getProductById(productDTO.getId());
        if (product == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此商品不存在");
        }

        int count = productMapper.deleteProduct(productDTO.getId());
        if (count != 1){
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED,"删除商品失败");
        }
    }

    @Override
    public void addProduct(BackgroundProductDTO backgroundProductDTO,UserLoginVO userLoginVO) {
        Product product = new Product();
        System.out.println(1255252);
        product.setCategoryId(backgroundProductDTO.getCategoryId());
        product.setItemType(backgroundProductDTO.getItemType());
        product.setTitle(backgroundProductDTO.getTitle());
        product.setSellPoint(backgroundProductDTO.getSellPoint());
        product.setPrice(backgroundProductDTO.getPrice());
        product.setNum(backgroundProductDTO.getNum());
        product.setImage(backgroundProductDTO.getImage());
        product.setStatus(backgroundProductDTO.getStatus());
        product.setPriority(backgroundProductDTO.getPriority());
        product.setCreatedUser(userLoginVO.getUsername());


        int count = productMapper.addProduct(product);
        if (count != 1){
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED,"添加商品失败");
        }
    }

    @Override
    public BackgroundProductVO getProductById(Integer id, UserLoginVO userLoginVO) {
        if (id == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "id不能为空");
        }
        Product product = productMapper.getProductById(id);
        if (product == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "此商品不存在");
        }
        BackgroundProductVO backgroundProductVO = new BackgroundProductVO();
        backgroundProductVO.setId(product.getId());
        backgroundProductVO.setCategoryId(product.getCategoryId());
        backgroundProductVO.setItemType(product.getItemType());
        backgroundProductVO.setTitle(product.getTitle());
        backgroundProductVO.setSellPoint(product.getSellPoint());
        backgroundProductVO.setPrice(product.getPrice());
        backgroundProductVO.setNum(product.getNum());
        backgroundProductVO.setImage(product.getImage());
        if (product.getStatus() == 1)
            backgroundProductVO.setStatus(true);
        else
            backgroundProductVO.setStatus(false);
        backgroundProductVO.setPriority(product.getPriority());
        backgroundProductVO.setCreatedUser(product.getCreatedUser());
        backgroundProductVO.setCreatedTime(product.getCreatedTime());
        backgroundProductVO.setModifiedUser(product.getModifiedUser());
        backgroundProductVO.setModifiedTime(product.getModifiedTime());
        return backgroundProductVO;


    }

    public Map<String, Integer> getCalProduct() {
        Map<String, Integer> map = new HashMap<>();
        List<Product> productList = productMapper.getAllProduct();
        System.out.println("商品列表\n" + productList);
        for (Product product: productList) {
            ProductCategory productCategory = productCategoryMapper.getCategoryById(product.getCategoryId());
            if (productCategory != null) {
                String categoryName = productCategory.getName();
                map.merge(categoryName, 1, Integer::sum);
            }

        }

        return map;
    }

    @Override
    public Integer countProduct() {
        Integer cnt = productMapper.countProduct();
        return cnt;
    }

}
