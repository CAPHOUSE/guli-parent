package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.eduorder.client.VodClient;
import com.atguigu.eduorder.entity.EduChapter;
import com.atguigu.eduorder.entity.EduCourse;
import com.atguigu.eduorder.entity.EduCourseDescription;
import com.atguigu.eduorder.entity.EduVideo;
import com.atguigu.eduorder.mapper.EduChapterMapper;
import com.atguigu.eduorder.mapper.EduCourseDescriptionMapper;
import com.atguigu.eduorder.mapper.EduCourseMapper;
import com.atguigu.eduorder.mapper.EduVideoMapper;
import com.atguigu.eduorder.service.EduCourseService;
import com.atguigu.eduorder.vo.*;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-09-23
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Resource
    private EduCourseDescriptionMapper courseDescriptionMapper;

    @Resource
    private EduVideoMapper videoMapper;

    @Resource
    private EduChapterMapper chapterMapper;

    @Autowired
    private VodClient vodClient;


    @Transactional
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);

        if (insert == 0){
            throw new GuliException(ResultCodeEnum.ERROR);
        }

        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(eduCourse.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());
        int result = courseDescriptionMapper.insert(courseDescription);

        if (result == 0){
            throw new GuliException(ResultCodeEnum.ERROR);
        }

        return eduCourse.getId();
    }


    @Override
    public CourseInfoVo getCourseById(String courseId) {
        EduCourse eduCourse = baseMapper.selectById(courseId);
        EduCourseDescription courseDescription = courseDescriptionMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    @Override
    public void updateCourse(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0){
            throw new GuliException(ResultCodeEnum.ERROR);
        }
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setId(courseInfoVo.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());
        int result = courseDescriptionMapper.updateById(courseDescription);
        if (result == 0){
            throw new GuliException(ResultCodeEnum.ERROR);
        }
    }

    @Override
    public CoursePublishVo getCoursePublishVo(String courseId) {
        return baseMapper.getCoursePublishVo(courseId);
    }

    @Override
    public IPage<EduCourse> pageQuery(Page<EduCourse> pageParam, CourseQueryVo courseQueryVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        String title = courseQueryVo.getTitle();
        String teacherId = courseQueryVo.getTeacherId();
        String subjectId = courseQueryVo.getSubjectId();
        String subjectParentId = courseQueryVo.getSubjectParentId();

        if (!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(teacherId)){
            wrapper.eq("teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(subjectId)){
            wrapper.eq("subject_id",subjectId);
        }
        if (!StringUtils.isEmpty(subjectParentId)){
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        wrapper.orderByDesc("gmt_create");
        return baseMapper.selectPage(pageParam,wrapper);
    }

    @Transactional
    @Override
    public void removeCourseById(String courseId) {

//        删除课程的小节
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId);
//        删除视频
        List<EduVideo> videoList = videoMapper.selectList(videoWrapper);
        List<String> eduVideoList = new ArrayList<>();
        for (EduVideo eduVideo : videoList) {
            if (!StringUtils.isEmpty(eduVideo.getVideoSourceId())){
                eduVideoList.add(eduVideo.getVideoSourceId());
            }
        }
        if (eduVideoList.size() > 0){
            Result result = vodClient.deleteBatch(eduVideoList);
            if (result.getCode() == 206){
                throw new GuliException(ResultCodeEnum.NATWORK_ERROR);
            }
        }

        Integer videoChapter = videoMapper.selectCount(videoWrapper);
        if (videoChapter != 0){
            int result = videoMapper.delete(videoWrapper);
            if (result == 0){
                throw new GuliException(ResultCodeEnum.ERROR);
            }
        }


//        删除章节
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id",courseId);
        Integer chapterCount = chapterMapper.selectCount(chapterWrapper);
        if (chapterCount != 0){
            int delete = chapterMapper.delete(chapterWrapper);
            if (delete == 0){
                throw new GuliException(ResultCodeEnum.ERROR);
            }
        }


//        删除课程
        int res = baseMapper.deleteById(courseId);
        if (res == 0){
            throw new GuliException(ResultCodeEnum.ERROR);
        }
    }

    @Override
    public IPage<EduCourse> getCourseListPage(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo) {
//        条件查询
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){  //判断一级id是否为空
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }

        if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())){  //判断二级id是否为空
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }

        if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){  //根据销量排序
            wrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){  //根据时间排序
            wrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())){  //根据价格排序
            wrapper.orderByDesc("price");
        }
        wrapper.eq("status","Normal");

        return baseMapper.selectPage(pageParam,wrapper);
    }

    @Override
    public CourseWebVo findCourseById(String courseId) {
        return baseMapper.findCourseById(courseId);
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String id) {
        return baseMapper.getBaseCourseInfo(id);
    }
}
