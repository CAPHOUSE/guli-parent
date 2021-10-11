package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduorder.entity.EduCourse;
import com.atguigu.eduorder.service.EduCourseService;
import com.atguigu.eduorder.vo.CourseInfoVo;
import com.atguigu.eduorder.vo.CoursePublishVo;
import com.atguigu.eduorder.vo.CourseQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-09-23
 */
@Api(description = "课程列表")
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    /**
     * 添加课程
     * @param courseInfoVo
     * @return
     */
    @ApiOperation("添加课程")
    @PostMapping("/addCourseInfo")
    public Result addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String courseId = courseService.saveCourseInfo(courseInfoVo);
        return Result.ok(courseId);
    }

    /**
     * 根据id查询课程
     * @param courseId
     * @return
     */
    @ApiOperation("根据id查询课程")
    @GetMapping("/getCourseById/{courseId}")
    public Result getCourseById(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseById(courseId);
        return Result.ok(courseInfoVo);
    }

    /**
     * 修改课程
     * @param courseInfoVo
     * @return
     */
    @ApiOperation("修改课程")
    @PostMapping("/updateCourse")
    public Result updateCourse(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourse(courseInfoVo);
        return Result.ok();
    }

    /**
     * 根据课程id回显信息
     * @param courseId
     * @return
     */
    @ApiOperation("根据课程id回显信息")
    @GetMapping("/getCoursePublishVo/{courseId}")
    public Result getCoursePublishVo(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(courseId);
        return Result.ok(coursePublishVo);
    }

    /**
     * 修改课程状态，最终发布
     * @param courseId
     * @return
     */
    @ApiOperation("修改课程状态，最终发布")
    @PutMapping("/saveCourseStatus/{courseId}")
    public Result saveCourseStatus(@PathVariable String courseId){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal"); //修改状态
        courseService.updateById(eduCourse);
        return Result.ok();
     }


    /**
     * 分页条件查询课程
     * @param page
     * @param limit
     * @param courseQueryVo
     * @return
     */
     @ApiOperation("分页条件查询课程")
     @PostMapping("/findCourseList/{page}/{limit}")
     public Result findCourseList(@PathVariable Long page,@PathVariable Long limit,
                                  @RequestBody(required = false) CourseQueryVo courseQueryVo){
         Page<EduCourse> pageParam = new Page<>(page,limit);
         IPage<EduCourse> pageModel = courseService.pageQuery(pageParam,courseQueryVo);
         return Result.ok(pageModel);
     }


    /**
     * 删除课程
     * @param courseId
     * @return
     */
     @ApiOperation("删除课程")
     @DeleteMapping("/removeCourseInfo/{courseId}")
     public Result removeCourseInfo(@PathVariable String courseId){
        courseService.removeCourseById(courseId);
        return Result.ok();
     }
}

