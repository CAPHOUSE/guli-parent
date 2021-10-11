package com.atguigu.staservice.service.impl;

import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-10-09
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void registerCount(String day) {
//        删除之前添加的日期记录
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        Integer count = ucenterClient.countRegister(day);

        StatisticsDaily str = new StatisticsDaily();
        str.setRegisterNum(count); //统计人数
        str.setDateCalculated(day); //统计日期
        str.setVideoViewNum(RandomUtils.nextInt(100,200));
        str.setLoginNum(RandomUtils.nextInt(100,200));
        str.setCourseNum(RandomUtils.nextInt(100,200));
        baseMapper.insert(str);
    }

    @Override
    public Map<String, Object> showData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> statisticsDailyList = baseMapper.selectList(wrapper);

//        返回数据 日期 + 日期的数量
        List<String> calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        for (StatisticsDaily statisticsDaily : statisticsDailyList) {
            calculatedList.add(statisticsDaily.getDateCalculated());

            if (type.equals("login_num")){
                numDataList.add(statisticsDaily.getLoginNum());
            }
            if (type.equals("register_num")){
                numDataList.add(statisticsDaily.getRegisterNum());
            }
            if (type.equals("video_view_num")){
                numDataList.add(statisticsDaily.getVideoViewNum());
            }
            if (type.equals("course_num")){
                numDataList.add(statisticsDaily.getCourseNum());
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("calculatedList",calculatedList);
        map.put("numDataList",numDataList);
        return map;
    }
}
