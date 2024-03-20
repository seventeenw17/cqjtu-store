package com.backend.springstore.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
@MapperScans({
        @MapperScan("com.backend.springstore.user.mapper"),
        @MapperScan("com.backend.springstore.address.mapper"),
        @MapperScan("com.backend.springstore.product.mapper"),
        @MapperScan("com.backend.springstore.cart.mapper"),
        @MapperScan("com.backend.springstore.favorite.mapper"),
        @MapperScan("com.backend.springstore.order.mapper"),
})
public class MyBatisConfig {

}
