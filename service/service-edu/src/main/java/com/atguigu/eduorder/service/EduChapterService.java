package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.EduChapter;
import com.atguigu.eduorder.vo.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author author
 * @since 2021-09-23
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideo(String courseId);

    void removeChapter(String chapterId);

}
