package com.atguigu.eduorder.controller.front;

import com.atguigu.commonutils.Result;
import com.atguigu.eduorder.entity.EduCourse;
import com.atguigu.eduorder.entity.EduTeacher;
import com.atguigu.eduorder.service.EduCourseService;
import com.atguigu.eduorder.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "前端列表")
@RestController
@RequestMapping("/eduservice/indexfront")
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    @Cacheable(value = "CourseTeacherList",key = "'findCourseTeacherList'")
    @ApiOperation("显示首页数据")
    @GetMapping("/findCourseTeacherList")
    public Result findCourseTeacherList(){
        Map<String,Object> map = new HashMap<>();
//        查询前八的热门课程
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.orderByDesc("view_count");
        courseWrapper.last("limit 8");
        List<EduCourse> courseList = courseService.list(courseWrapper);

//        查询前四的讲师
        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByDesc("id");
        teacherWrapper.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(teacherWrapper);

        map.put("courseList",courseList);
        map.put("teacherList",teacherList);
        return Result.ok(map);
    }
}
