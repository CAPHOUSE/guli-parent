package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.EduCourse;
import com.atguigu.eduorder.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author author
 * @since 2021-09-23
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseById(String courseId);

    void updateCourse(CourseInfoVo courseInfoVo);

    CoursePublishVo getCoursePublishVo(String courseId);

    IPage<EduCourse> pageQuery(Page<EduCourse> pageParam, CourseQueryVo courseQueryVo);

    void removeCourseById(String courseId);

    IPage<EduCourse> getCourseListPage(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo);

    CourseWebVo findCourseById(String courseId);

    CourseWebVo getBaseCourseInfo(String id);

}
