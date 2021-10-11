package com.atguigu.eduorder.client;

import com.atguigu.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "service-vod",fallback = VodClientImpl.class) //fallback执行熔断
public interface VodClient {

//    根据视频id删除视频
    @DeleteMapping("/vod/video/removeVideo/{id}")
    public Result removeVideo(@PathVariable("id") String id);

//    删除多个视频
    @DeleteMapping("/vod/video/deleteBatch")
    public Result deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
