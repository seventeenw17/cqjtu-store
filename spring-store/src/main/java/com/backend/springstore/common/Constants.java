package com.backend.springstore.common;

import java.util.Arrays;
import java.util.List;

/**
 * 常量封装
 */
public class Constants {
    public static final int IS_NOT_DELETED = 0;
    public static final int IS_DELETED = 1;
    public static final int HASH_TIME = 6;
    public static final int MAX_FILE_SIZE = 1024 * 1024 * 2;
    public static final int DEFAULT_ADDRESS = 1;
    public static final int MAX_ADDRESS_ITEMS_COUNT = 10;
    public static final int MAX_CART_ITEMS_COUNT = 50;
    private static final int NOT_DEFAULT_ADDRESS = 0;
    /* pv */
    public static final String OPERATION_TYPE_PV = "pv";
    /* 购物车 */
    public static final String OPERATION_TYPE_CART = "cart";
    /* 购买 */
    public static final String OPERATION_TYPE_BUY = "buy";
    /* 收藏 */
    public static final String OPERATION_TYPE_FAVORITE = "favorite";
    public static final List<String> IMAGE_FILE_TYPE = Arrays.asList("jpg", "jpeg", "png");
}
