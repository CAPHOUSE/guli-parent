package com.atguigu.eduorder.service.impl;

import com.atguigu.eduorder.entity.EduTeacher;
import com.atguigu.eduorder.mapper.EduTeacherMapper;
import com.atguigu.eduorder.service.EduTeacherService;
import com.atguigu.eduorder.vo.TeachQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-09-17
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    /**
     * 分页条件查询
     * @param pageParam
     * @param
     * @return
     */
    @Override
    public IPage<EduTeacher> listPage(Page<EduTeacher> pageParam, TeachQueryVo teachQueryVo) {
//        构建查询方法
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();

//        获取值
        String name = teachQueryVo.getName();
        Integer level = teachQueryVo.getLevel();
        String end = teachQueryVo.getEnd();
        String begin = teachQueryVo.getBegin();

//        判断条件是否存在
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin); //大于等于
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);   //小于等于
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        return baseMapper.selectPage(pageParam,wrapper);

    }


    @Override
    public IPage<EduTeacher> findTeacherList(Page<EduTeacher> pageParam) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return baseMapper.selectPage(pageParam,wrapper);
    }
}
