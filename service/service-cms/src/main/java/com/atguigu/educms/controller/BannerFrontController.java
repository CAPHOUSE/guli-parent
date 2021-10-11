package com.atguigu.educms.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前台控制器
 * </p>
 *
 * @author author
 * @since 2021-09-28
 */

@RestController
@RequestMapping("/educms/bannerfront")
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    @GetMapping("/findBannerList")
    public Result findBannerList(){
       List<CrmBanner> bannerList =  bannerService.findBannerList();
       return Result.ok(bannerList);
    }

}

