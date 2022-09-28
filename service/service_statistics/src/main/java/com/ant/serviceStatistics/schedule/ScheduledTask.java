package com.ant.serviceStatistics.schedule;

import com.ant.serviceStatistics.service.StatisticsDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;

@Component
public class ScheduledTask {
    private final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private final StatisticsDailyService statisticsDailyService;

    public ScheduledTask(StatisticsDailyService statisticsDailyService) {
        this.statisticsDailyService = statisticsDailyService;
    }

//    @Scheduled(cron = "0/5 * * * * ? ")  // 每五秒执行一次
//    public void task(){
//        System.out.println("***************task测试执行");
//    }


    @Scheduled(cron = "0 0 1 * * ?")
    public void updateStatisticsDaily(){
        LocalDate now = LocalDate.now();
        LocalDate date = now.minus(1, ChronoUnit.DAYS);
        log.info("ScheduledTask to update updateStatisticsDaily:{}",date.toString());

        statisticsDailyService.createStatisticsByDate(date.toString());

    }


}
