package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduorder.dto.SubjectDto;
import com.atguigu.eduorder.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-09-22
 */
@Api(description = "科目列表")
@RestController
@RequestMapping("/eduservice/subject")
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;


//    添加课程分类
    @ApiOperation("添加课程分类")
    @PostMapping("/addSubject")
    public Result addSubject(MultipartFile file){
        eduSubjectService.saveSubject(file);
        return Result.ok();
    }

//    课程分类列表
    @ApiOperation("课程分类列表")
    @GetMapping("/getSubjectList")
    public Result getSubjectList(){
       List<SubjectDto> list =   eduSubjectService.findSubjectList();
        return Result.ok(list);
    }
}

