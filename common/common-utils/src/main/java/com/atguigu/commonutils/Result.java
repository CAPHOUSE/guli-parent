package com.atguigu.commonutils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    private Result(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    private Result(ResultCodeEnum resultCodeEnum, T data) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
        this.data = data;
    }

    private static <T> Result<T> build(T data){
        Result<T> result = new Result<>();
        if (null != data){
            result.setData(data);
        }
        return result;
    }

    private static <T> Result<T> build(T data,ResultCodeEnum resultCodeEnum){
        Result<T> result = build(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    private static <T> Result<T> build(T data,Integer code, String message){
        Result<T> result = build(data);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


    public static <T> Result<T> ok(){
        return Result.ok(null);
    }

    public static <T> Result<T> ok(T data){
        return Result.build(data,ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> fail(){
        return Result.fail(null);
    }

    public static <T> Result<T> fail(T data){
        return Result.build(data,ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> setResultCodeEnum(ResultCodeEnum resultCodeEnum){
        return Result.build(null,resultCodeEnum);
    }

    public static <T> Result<T> setResultCodeEnum(Integer code,String message){
        return Result.build(null,code,message);
    }

}
