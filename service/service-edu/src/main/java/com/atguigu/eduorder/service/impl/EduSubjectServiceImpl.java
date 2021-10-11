package com.atguigu.eduorder.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduorder.dto.SubjectDto;
import com.atguigu.eduorder.entity.EduSubject;
import com.atguigu.eduorder.excel.SubjectData;
import com.atguigu.eduorder.listener.SubjectExcelListener;
import com.atguigu.eduorder.mapper.EduSubjectMapper;
import com.atguigu.eduorder.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-09-22
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file) {
        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(this)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SubjectDto> findSubjectList() {
        List<SubjectDto> subjectDtoList = new ArrayList<>();

//        查询所有科目
        List<EduSubject> eduSubjectList = baseMapper.selectList(null);

//        封装一级分类
        for (EduSubject eduSubject : eduSubjectList) {
            if (eduSubject.getParentId().equals("0")){
                SubjectDto subjectDto = new SubjectDto();
                BeanUtils.copyProperties(eduSubject,subjectDto);
                subjectDtoList.add(subjectDto);
            }
        }

//        封装二级分类
        for (SubjectDto subjectDto : subjectDtoList) {
            List<SubjectDto> childrenList = new ArrayList<>();
            for (EduSubject eduSubject : eduSubjectList) {
                if (eduSubject.getParentId().equals(subjectDto.getId())){
                    SubjectDto twoSubjectDto = new SubjectDto();
                    BeanUtils.copyProperties(eduSubject,twoSubjectDto);
                    childrenList.add(twoSubjectDto);
                }
                continue;
            }
            subjectDto.setChildren(childrenList);
        }
        return subjectDtoList;
    }


}
