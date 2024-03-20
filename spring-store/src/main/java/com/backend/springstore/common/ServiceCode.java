package com.backend.springstore.common;

import lombok.Getter;

/**
 * 状态码封装
 */
public enum ServiceCode {

    /**
     * 成功
     */
    OK(20000),
    /**
     * 错误：请求参数格式有误
     */
    ERROR_BAD_REQUEST(40000),
    /**
     * 错误：登录失败，用户名或密码错
     */
    ERROR_UNAUTHORIZED(40100),
    /**
     * 错误：登录失败，账号被禁用
     */
    ERROR_UNAUTHORIZED_DISABLED(40101),
    /**
     * 错误：无权限
     */
    ERROR_FORBIDDEN(40300),
    /**
     * 错误：数据不存在
     */
    ERROR_NOT_FOUND(40400),
    /**
     * 错误：数据冲突
     */
    ERROR_CONFLICT(40900),
    /**
     * 错误：数据冲突，已经存在
     */
    ERROR_EXISTS(40901),
    /**
     * 错误：数据冲突，已经关联
     */
    ERROR_IS_ASSOCIATED(40902),
    /**
     * 错误：插入数据错误
     */
    ERROR_SAVE_FAILED(50000),
    /**
     * 错误：删除数据错误
     */
    ERROR_DELETE_FAILED(50100),
    /**
     * 错误：修改数据错误
     */
    ERROR_UPDATE_FAILED(50200),
    /**
     * 文件上载错误
     */
    ERROR_FILE_UPLOAD(50300),
    /**
     * 错误：JWT已过期
     */
    ERROR_JWT_EXPIRED(60000),
    /**
     * 错误：验证签名失败
     */
    ERROR_JWT_SIGNATURE(60100),
    /**
     * 错误：JWT格式错误
     */
    ERROR_JWT_MALFORMED(60200),
    /**
     * 错误：JWT已经登出
     */
    ERROR_JWT_LOGOUT(60300),
    /**
     * 错误：未知错误
     */
    ERROR_UNKNOWN(99999);

    private Integer value;

    ServiceCode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
