package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.ResultCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GuliException extends RuntimeException {


    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "异常信息")
    private String message;

    public GuliException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
}
