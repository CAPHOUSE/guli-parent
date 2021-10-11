package com.atguigu.eduorder.mapper;

import com.atguigu.eduorder.entity.EduCourse;
import com.atguigu.eduorder.vo.CoursePublishVo;
import com.atguigu.eduorder.vo.CourseWebVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-09-23
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getCoursePublishVo(String courseId);

    CourseWebVo findCourseById(String courseId);

    CourseWebVo getBaseCourseInfo(String id);
}
