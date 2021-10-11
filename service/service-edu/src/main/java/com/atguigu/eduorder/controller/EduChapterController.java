package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduorder.entity.EduChapter;
import com.atguigu.eduorder.service.EduChapterService;
import com.atguigu.eduorder.vo.ChapterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-09-23
 */
@Api(description = "章节列表")
@RestController
@RequestMapping("/eduservice/chapter")
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    /**
     * 根据课程id查询章节以及小节
     * @param courseId
     * @return
     */
    @ApiOperation("根据课程id查询章节以对应的及小节")
    @GetMapping("/getChapterVideo/{courseId}")
    public Result getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list = chapterService.getChapterVideo(courseId);
        return Result.ok(list);
    }

    /**
     * 添加章节
     * @param eduChapter
     * @return
     */
    @ApiOperation("添加章节")
    @PostMapping("/saveChapter")
    public Result saveChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return Result.ok();
    }


    /**
     * 根据章节id查询
     * @param chapterId
     * @return
     */
    @ApiOperation("根据章节id查询")
    @GetMapping("/getChapterById/{chapterId}")
    public Result getChapterById(@PathVariable String chapterId){
        EduChapter chapter = chapterService.getById(chapterId);
        return Result.ok(chapter);
    }

    /**
     * 修改章节
     * @param eduChapter
     * @return
     */
    @ApiOperation("修改章节")
    @PostMapping("/updateChapter")
    public Result updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return Result.ok();
    }


    /**
     * 删除章节
     * @param chapterId
     * @return
     */
    @ApiOperation("删除章节")
    @DeleteMapping("/removeChapter/{chapterId}")
    public Result removeChapter(@PathVariable String chapterId){
        chapterService.removeChapter(chapterId);
        return Result.ok();
    }

}

