package com.atguigu.eduorder.controller.front;

import com.atguigu.commonutils.Result;
import com.atguigu.eduorder.entity.EduCourse;
import com.atguigu.eduorder.entity.EduTeacher;
import com.atguigu.eduorder.service.EduCourseService;
import com.atguigu.eduorder.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "前台讲师接口")
@RestController
@RequestMapping("/eduservice/teeacherfront")
public class TeacherFrontController {


    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    /**
     * 教师查询带分页
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation("教师查询带分页")
    @GetMapping("/findTeacherList/{page}/{limit}")
    public Result findTeacherList(@PathVariable Long page,@PathVariable Long limit){
       Page<EduTeacher> pageParam = new Page<>(page,limit);
       IPage<EduTeacher> pageModel = teacherService.findTeacherList(pageParam);
       return Result.ok(pageModel);
    }


    /**
     * 根据id查询讲师
     * @param teacherId
     * @return
     */
    @ApiOperation("根据id查询讲师")
    @GetMapping("/findTeacherById/{teacherId}")
    public Result findTeacherById(@PathVariable String teacherId){
//        查询讲师的基本信息
        EduTeacher teacher = teacherService.getById(teacherId);

//        查询讲师课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = courseService.list(wrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("teacher",teacher);
        map.put("courseList",courseList);

        return Result.ok(map);
    }

}
