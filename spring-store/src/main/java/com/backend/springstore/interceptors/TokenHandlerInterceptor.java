package com.backend.springstore.interceptors;

import com.backend.springstore.security.JWTUtils;
import com.backend.springstore.common.ServiceCode;
import com.backend.springstore.common.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Slf4j
//public class TokenHandlerInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.debug("开始拦截......");
//        // 获取用户token
//        String token = request.getHeader("Authorization");
//        System.out.println("token:"+token);
//        // token为空
//        if (token == null || token.isEmpty()) {
//            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "token不能为空！");
//        }
//        // 检验token合法性
//        JWTUtils.parseToken(token);
//        return HandlerInterceptor.super.preHandle(request, response, handler);
//    }
//}
@Slf4j
public class TokenHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("开始拦截......");


        // 获取用户token
        System.out.println("Request:"+request);
        String token = request.getHeader("Authorization");
        System.out.println("token:"+token);
        // token为空
        if (token == null || token.isEmpty()) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "token不能为空！");
        }
        // 检验token合法性
        JWTUtils.parseToken(token);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


}

