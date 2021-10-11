package com.atguigu.eduorder.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.eduorder.entity.EduSubject;
import com.atguigu.eduorder.excel.SubjectData;
import com.atguigu.eduorder.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    //注入subjectService
    public EduSubjectService subjectService;
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

//    读取excel内容
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData == null){
            throw new GuliException(ResultCodeEnum.ERROR);
        }
//        判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        if (existOneSubject == null){ //没有一级分类，进行添加
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName()); //设置一级分类名称
            subjectService.save(existOneSubject);
        }

//        获取一级分类的id值
        String pid = existOneSubject.getId();

//      判断二级分类是否重复
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(), pid);
        if (null == existTwoSubject){
            existOneSubject = new EduSubject();
            existOneSubject.setParentId(pid);
            existOneSubject.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(existOneSubject);
        }

    }

//    判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        return subjectService.getOne(wrapper);
    }

//    判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        return subjectService.getOne(wrapper);
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
