package com.backend.springstore.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * 封装返回类型
 */
@Data
public class Result<T> {
    // 状态码
    private int code;
    // 信息提示
    private String message;
    // 数据
    private T data;

    public static <T> Result<T> ok(T data){
        Result<T> result = new Result<>();
        result.setCode(ServiceCode.OK.getValue());
        result.setData(data);
        result.message="ok";
        return result;
    }

    /*
     * 操作成功，不携带数据
     */
    public static Result<Void> ok(){
        return ok(null);
    }

    /*
     * 操作失败
     */
    public static<Void> Result<Void> fail(ServiceCode serviceCode, String message){
        Result<Void> result= new Result<>();
        result.setCode(serviceCode.getValue());
        result.setMessage(message);
        return result;
    }

    /*
     * 由于业务异常，操作失败
     */
    public static Result<Void> fail(ServiceException e){
        return fail(e.getServiceCode(), e.getMessage());
    }

}
