package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.vo.LoginVo;
import com.atguigu.educenter.vo.RegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-09-29
 */
@Api(description = "登录接口")
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {


    @Autowired
    private UcenterMemberService memberService;


    @ApiOperation("会员登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        String token = memberService.login(loginVo);
        return Result.ok(token);
    }


    @ApiOperation("会员注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return Result.ok();
    }

    @ApiOperation("检验token令牌")
    @GetMapping("/getUserInfo")
    public Result getLoginInfo(HttpServletRequest request){
        String memberId= JwtUtils.getMemberIdByJwtToken(request);
        UcenterMember member = memberService.getById(memberId);
        return Result.ok(member);
    }

    @ApiOperation("根据用户id获取用户信息")
    @PostMapping("/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember ucenterMember = memberService.getById(id);
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(ucenterMember,ucenterMemberOrder);
        return ucenterMemberOrder;
    }


    @ApiOperation("查询某一天的注册人数")
    @GetMapping("/countRegister/{day}")
    public Integer countRegister(@PathVariable String day){
        return memberService.countRegister(day);
    }
}

