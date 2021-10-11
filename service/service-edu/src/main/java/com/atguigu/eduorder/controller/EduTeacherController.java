package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.Result;
import com.atguigu.eduorder.entity.EduTeacher;
import com.atguigu.eduorder.service.EduTeacherService;
import com.atguigu.eduorder.vo.TeachQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-09-17
 */
@Api(description = "讲师列表")
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;

    /**
     * 查询讲师表所有数据
     */
    @ApiOperation("查询所有讲师")
    @GetMapping("/findAll")
    public Result findAllTeach(){
        List<EduTeacher> eduTeacherList = eduTeacherService.list(null);
        return Result.ok(eduTeacherList);
    }

    /**
     * 逻辑删除讲师功能
     */
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("{id}")
    public Result removeTeach(@PathVariable String id){
        return eduTeacherService.removeById(id) ? Result.ok() : Result.fail();
    }

    /**
     * 分页条件查询
     */
    @ApiOperation("分页条件查询")
    @PostMapping("/pageTeach/{page}/{limit}")
    public Result pageListTeach(@PathVariable Long page, @PathVariable Long limit,
                                @RequestBody(required = false) TeachQueryVo teachQueryVo){
        Page<EduTeacher> pageParam = new Page<>(page,limit);
        IPage<EduTeacher> pageModel = eduTeacherService.listPage(pageParam,teachQueryVo);
        return Result.ok(pageModel);
    }


    /**
     * 添加讲师
     */
    @ApiOperation("添加讲师")
    @PostMapping("/saveTeach")
    public Result saveTeach(@RequestBody EduTeacher eduTeacher){
       return eduTeacherService.save(eduTeacher) ? Result.ok() : Result.fail();
    }


    /**
     * 根据讲师id查询
     */
    @ApiOperation("根据讲师id查询")
    @GetMapping("/getTeacher/{id}")
    public Result getTeacher(@PathVariable String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return Result.ok(eduTeacher);
    }

    /**
     * 修改讲师信息
     */
    @ApiOperation("修改讲师信息")
    @PostMapping("/updateTeacher")
    public Result updateTeacher(@RequestBody EduTeacher eduTeacher){
        return eduTeacherService.updateById(eduTeacher) ? Result.ok() : Result.fail();
    }


}

