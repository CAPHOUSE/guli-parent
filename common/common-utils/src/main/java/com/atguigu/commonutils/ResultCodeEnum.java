package com.atguigu.commonutils;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"操作成功!"),
    FAIL(201,"操作失败!"),
    PWD_ERROR(202,"用户名或密码错误!"),
    LOGIN_AUTH_ERROR(401, "用户未登录"),
    USER_LOCK(203,"账号已被锁定!"),
    AUDIO_ERROR(205,"未知音频"),
    AVATAR_ERROR(204,"未知头像!"),
    ERROR(206,"系统维护中,请稍后再试!"),
    NATWORK_ERROR(207,"连接超时,请稍后再试!"),
    CODE_ERROR(208,"短信验证码发送失败!"),
    CODE_FREQUENT(209,"操作过于频繁,请稍后再试!"),
    NOTWORK_ERROR(210,"用户不存在!"),
    USERINFO_ERROR(211,"参数错误!"),
    IS_MOBILE(212,"用户已经存在!"),
    VERIFICATION_ERROR(213,"验证码错误"),
    WXPAY_SUCCESS(214,"微信支付成功"),
    WXPAY_INFO(215,"微信支付中"),
    WXPAY_ERROR(216,"微信支付失败");


    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
