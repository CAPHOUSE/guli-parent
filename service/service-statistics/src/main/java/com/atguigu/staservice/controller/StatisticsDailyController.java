package com.atguigu.staservice.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.staservice.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-10-09
 */
@Api(description = "统计接口")
@RestController
@RequestMapping("/staservice/str")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService strService;

//    统计某一天的注册人数
    @ApiOperation("统计某一天的注册人数")
    @GetMapping("/registerCount/{day}")
    public Result registerCount(@PathVariable String day){
       strService.registerCount(day);
       return Result.ok();
    }

//    图表显示
    @ApiOperation("查询数据")
    @GetMapping("/showData/{type}/{begin}/{end}")
    public Result showData(@PathVariable String type,@PathVariable String begin,
                           @PathVariable String end){
      Map<String,Object> map = strService.showData(type,begin,end);
      return Result.ok(map);
    }
}

