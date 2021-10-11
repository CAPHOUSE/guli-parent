package com.atguigu.educms.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 后台控制器
 * </p>
 *
 * @author author
 * @since 2021-09-28
 */

@Api(description = "banner列表")
@RestController
@RequestMapping("/educms/banneradmin")
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;


    @ApiOperation("分页查询banner")
    @PostMapping("/getBanner/{page}/{limit}")
    public Result getBannerPage(@PathVariable Long page,@PathVariable Long limit){
        Page<CrmBanner> pageModel = new Page<>();
        return Result.ok();
    }


    @ApiOperation("添加banner")
    @PostMapping("/saveBanner")
    public Result saveBanner(@RequestBody CrmBanner banner){
        bannerService.save(banner);
        return Result.ok();
    }

    @ApiOperation("修改banner")
    @PostMapping("/updateBanner")
    public Result updateBanner(@RequestBody CrmBanner banner){
        bannerService.updateById(banner);
        return Result.ok();
    }

    @ApiOperation("删除banner")
    @PostMapping("/removeBanner/{id}")
    public Result removeBanner(@PathVariable String id){
        bannerService.removeById(id);
        return Result.ok();
    }
}

