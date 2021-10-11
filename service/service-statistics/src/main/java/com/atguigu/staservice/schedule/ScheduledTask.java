package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService strService;


    @Scheduled(cron = "0 0 1 * * ?") //每天凌晨一点执行程序
    public void task(){
        strService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }
}
