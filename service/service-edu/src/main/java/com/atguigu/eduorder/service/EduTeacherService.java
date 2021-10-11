package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.EduTeacher;
import com.atguigu.eduorder.vo.TeachQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author author
 * @since 2021-09-17
 */
public interface EduTeacherService extends IService<EduTeacher> {

    /**
     * 分页条件查询
     * @param pageParam
     * @param
     * @return
     */
    IPage<EduTeacher> listPage(Page<EduTeacher> pageParam, TeachQueryVo teachQueryVo);

    IPage<EduTeacher> findTeacherList(Page<EduTeacher> pageParam);

}
