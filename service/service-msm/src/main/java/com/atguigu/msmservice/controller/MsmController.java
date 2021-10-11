package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.msmservice.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api("短信接口")
@RestController
@RequestMapping("/edumsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("短信发送")
    @GetMapping("/send/{phone}")
    public Result sendMsm(@PathVariable String phone){
        String code = (String) redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)){
            return Result.setResultCodeEnum(ResultCodeEnum.CODE_FREQUENT);
        }
        boolean isSend =  msmService.send(phone);
      if (isSend) {
          return Result.ok();
      }
      return Result.setResultCodeEnum(ResultCodeEnum.CODE_ERROR);
    }
}
