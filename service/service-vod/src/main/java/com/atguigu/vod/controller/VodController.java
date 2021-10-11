package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import com.atguigu.vod.utils.InitVodClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(description = "视频操作")
@RestController
@RequestMapping("/vod/video")
public class VodController {

    @Autowired
    private VodService vodService;


    /**
     * 上传视频
     * @param file
     * @return
     */
    @ApiOperation("上传视频")
    @PostMapping("/uploadVideo")
    public Result uploadVideo(MultipartFile file){
        String videoId = vodService.uploadVideo(file);
        return Result.ok(videoId);
    }

    /**
     * 根据视频id删除视频
     * @param id
     * @return
     */
    @ApiOperation("根据视频id删除视频")
    @DeleteMapping("/removeVideo/{id}")
    public Result removeVideo(@PathVariable String id){
        vodService.removeVideo(id);
        return Result.ok();
    }


    /**
     * 删除多个视频
     * @return
     */
    @ApiOperation("删除多个视频")
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeVideoBatch(videoIdList);
        return Result.ok();
    }


    /**
     * 根据视频id获取凭证
     * @return
     */
    @ApiOperation("根据视频id获取凭证")
    @GetMapping("/getPlayAuth/{id}")
    public Result getPlayAuth(@PathVariable String id){
        try {
            //获取阿里云存储相关常量
            String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;
            String accessKeySecret = ConstantVodUtils.ACCESS_KEY_SECRET;
            //初始化
            DefaultAcsClient client = InitVodClient.initVodClient(accessKeyId, accessKeySecret);
            //请求
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(id);
            //响应
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            //得到播放凭证
            String playAuth = response.getPlayAuth();


            return Result.ok(playAuth);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.ERROR);
        }
    }
}
