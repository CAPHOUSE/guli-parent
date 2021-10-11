package com.atguigu.eduorder.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.Result;
import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.eduorder.client.OrderClient;
import com.atguigu.eduorder.entity.EduCourse;
import com.atguigu.eduorder.service.EduChapterService;
import com.atguigu.eduorder.service.EduCourseService;
import com.atguigu.eduorder.vo.ChapterVo;
import com.atguigu.eduorder.vo.CourseFrontVo;
import com.atguigu.eduorder.vo.CourseWebVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "前台课程接口")
@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    /**
     * 查询课程分类
     * @param page
     * @param limit
     * @param courseFrontVo
     * @return
     */
    @ApiOperation("查询课程分类")
    @PostMapping("/getCourseListPage/{page}/{limit}")
    public Result getCourseListPage(@PathVariable Long page,@PathVariable Long limit,
                                    @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> pageParam = new Page<>(page,limit);
        IPage<EduCourse> pageModel = courseService.getCourseListPage(pageParam,courseFrontVo);
        return Result.ok(pageModel);
    }


    /**
     * 根据课程id查询课程信息
     * @param courseId
     * @return
     */
    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("/getCourseById/{courseId}")
    public Result getCourseById(@PathVariable String courseId, HttpServletRequest request){
//        查询课程信息
        CourseWebVo courseWebVo =  courseService.findCourseById(courseId);

//        根据课程id查询章节和小节
        List<ChapterVo> chapterVideo = chapterService.getChapterVideo(courseId);

//        根据课程id和用户id查询用户是否支付
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean isPay = orderClient.getByCourse(courseId, memberId);

        Map<String, Object> map = new HashMap<>();
        map.put("courseWebVo",courseWebVo);
        map.put("chapterVideo",chapterVideo);
        map.put("isPay",isPay);

        return Result.ok(map);
    }


    @ApiOperation("根据课程id查询课程信息")
    @PostMapping("/getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id){
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseWebVo,courseWebVoOrder);
        return courseWebVoOrder;
    }
}
