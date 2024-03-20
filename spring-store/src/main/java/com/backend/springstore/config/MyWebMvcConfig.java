package com.backend.springstore.config;

import com.backend.springstore.interceptors.TokenHandlerInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc配置类 | 配置拦截器
 */
@SpringBootConfiguration
public class MyWebMvcConfig implements WebMvcConfigurer {
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenHandlerInterceptor())
                .addPathPatterns("/user/**") // 拦截用户URL
                .addPathPatterns("/address/**") // 拦截地址URL
                .addPathPatterns("/cart/**") // 拦截购物车URL
                .addPathPatterns("manageOrder/**") // 拦截 订单管理 URL
                .addPathPatterns("/favorite/**")
//                .addPathPatterns("/order/**")
                .excludePathPatterns("/user/login",
                        "/user/register",
                        "/user/count",
                        "/cart/count",
                        "/favorite/count");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 所有请求路径
//                .allowedOriginPatterns("*") // 所有来源
//                .allowedHeaders("*") // 所有请求头
//                .allowedMethods("*") // 所有请求方式
//                .allowCredentials(true) // 允许凭证
//                .maxAge(3600); // 有效期
//    }

}
