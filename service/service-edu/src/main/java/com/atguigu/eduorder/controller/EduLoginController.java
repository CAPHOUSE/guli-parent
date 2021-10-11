package com.atguigu.eduorder.controller;

import com.atguigu.commonutils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(description = "后台用户登录")
@RestController
@RequestMapping("/eduservice/user")
public class EduLoginController {


    /**
     * 登录
     */
    @ApiOperation("后台登录功能")
    @PostMapping("/login")
    public Result login(){
      return Result.ok("token");
    }


    /**
     * 授权
     */
    @GetMapping("/info")
    public Result info(){
        Map<String, String> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }
}
