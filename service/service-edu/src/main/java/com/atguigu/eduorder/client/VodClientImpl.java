package com.atguigu.eduorder.client;

import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.ResultCodeEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 熔断机制
 */
@Component
public class VodClientImpl implements VodClient {

    @Override
    public Result removeVideo(String id) {
        return Result.setResultCodeEnum(ResultCodeEnum.NATWORK_ERROR);
    }

    @Override
    public Result deleteBatch(List<String> videoIdList) {
        return Result.setResultCodeEnum(ResultCodeEnum.NATWORK_ERROR);
    }
}
