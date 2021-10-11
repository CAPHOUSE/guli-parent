package com.atguigu.eduorder.service;

import com.atguigu.eduorder.dto.SubjectDto;
import com.atguigu.eduorder.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author author
 * @since 2021-09-22
 */
public interface EduSubjectService extends IService<EduSubject> {

    void saveSubject(MultipartFile file);

    List<SubjectDto> findSubjectList();

}
