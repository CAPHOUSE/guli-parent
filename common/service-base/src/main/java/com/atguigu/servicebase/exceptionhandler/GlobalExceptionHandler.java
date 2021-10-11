package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    //全局异常
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.setResultCodeEnum(ResultCodeEnum.ERROR);
    }

    //特定异常
    @ResponseBody
    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.fail();
    }


    //自定义异常
    @ResponseBody
    @ExceptionHandler(GuliException.class)
    public Result error(GuliException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.setResultCodeEnum(e.getCode(),e.getMessage());
    }



}
