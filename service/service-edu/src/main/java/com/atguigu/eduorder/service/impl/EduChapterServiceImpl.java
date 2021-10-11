package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.eduorder.entity.EduChapter;
import com.atguigu.eduorder.entity.EduVideo;
import com.atguigu.eduorder.mapper.EduChapterMapper;
import com.atguigu.eduorder.mapper.EduVideoMapper;
import com.atguigu.eduorder.service.EduChapterService;
import com.atguigu.eduorder.vo.ChapterVo;
import com.atguigu.eduorder.vo.VideoVo;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Resource
    private EduVideoMapper videoMapper;

    @Override
    public List<ChapterVo> getChapterVideo(String courseId) {
//        课程下的所有章节
        List<ChapterVo> chapterVoList = new ArrayList<>();
//        查询课程下的所有章节
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapper);
//        将查询的章节信息复制到Vo中
        for (EduChapter eduChapter : eduChapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            chapterVoList.add(chapterVo);
        }

//        查询章节下的所有小节
        for (ChapterVo chapterVo : chapterVoList) {
//        所有小节的集合
            List<VideoVo> videoVoList = new ArrayList<>();
            QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
            videoWrapper.eq("chapter_id",chapterVo.getId());
//            章节下的小节信息
            List<EduVideo> videoList = videoMapper.selectList(videoWrapper);
//            将查询的小节信息复制到Vo中
            for (EduVideo eduVideo : videoList) {
                VideoVo videoVo = new VideoVo();
                BeanUtils.copyProperties(eduVideo,videoVo);
                videoVoList.add(videoVo);
            }
            chapterVo.setChildren(videoVoList);
        }
//        返回集合
        return chapterVoList;
    }

    @Transactional
    @Override
    public void removeChapter(String chapterId) {
        int delete = baseMapper.deleteById(chapterId);
        if (delete == 0){
            throw new GuliException(ResultCodeEnum.ERROR);
        }
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        Integer count = videoMapper.selectCount(wrapper);
        if (count > 0){
            int result = videoMapper.delete(wrapper);
            if (result == 0){
                throw new GuliException(ResultCodeEnum.ERROR);
            }
        }

    }
}
