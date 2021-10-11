package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.eduorder.client.VodClient;
import com.atguigu.eduorder.entity.EduVideo;
import com.atguigu.eduorder.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-09-23
 */
@Api(description = "小节列表")
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    /**
     * 添加章节
     * @param eduVideo
     * @return
     */
    @ApiOperation("添加小节")
    @PostMapping("/saveVideo")
    public Result saveVideo(@RequestBody EduVideo eduVideo){
        eduVideo.setId(eduVideo.getVideoSourceId());
        videoService.save(eduVideo);
        return Result.ok();
    }


    /**
     *  todo 删除小节的时候连带删除视频
     * @param id
     * @return
     */
    @Transactional
    @ApiOperation("删除小节")
    @DeleteMapping("/removeVideo/{id}")
    public Result removeVideo(@PathVariable String id){
        EduVideo video = videoService.getById(id);
        String videoSourceId = video.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)){
            Result result = vodClient.removeVideo(videoSourceId);
            System.out.println(result);
            if (result.getCode() == 206){
                return Result.setResultCodeEnum(ResultCodeEnum.NATWORK_ERROR);
            }
        }
        videoService.removeById(id);
        return Result.ok();
    }


    /**
     * 修改小节
     * @param eduVideo
     * @return
     */
    @ApiOperation("修改小节")
    @PostMapping("/updateVideo")
    public Result updateVideo(@RequestBody EduVideo eduVideo){
        videoService.updateById(eduVideo);
        return Result.ok();
    }

    /**
     * 根据id查询小节
     * @param videoId
     * @return
     */
    @ApiOperation("根据id查询小节")
    @GetMapping("/getVideoById/{videoId}")
    public Result getVideoById(@PathVariable String videoId){
        EduVideo eduVideo = videoService.getById(videoId);
        return Result.ok(eduVideo);
    }
}

