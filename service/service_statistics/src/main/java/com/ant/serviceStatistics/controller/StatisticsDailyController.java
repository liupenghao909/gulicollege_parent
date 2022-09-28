package com.ant.serviceStatistics.controller;


import com.ant.commonutils.R;
import com.ant.serviceStatistics.service.StatisticsDailyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-09-24
 */
@RestController
@RequestMapping("/serviceStatistics/statisticsDaily")

public class StatisticsDailyController {
    private final Logger log = LoggerFactory.getLogger(StatisticsDailyController.class);

    private final StatisticsDailyService statisticsDailyService;

    public StatisticsDailyController(StatisticsDailyService statisticsDailyService) {
        this.statisticsDailyService = statisticsDailyService;
    }

    @GetMapping("/create/statistics/{day}")
    public R createStatisticsByDate(@PathVariable("day") String day){
        statisticsDailyService.createStatisticsByDate(day);

        return R.ok();
    }

    @PostMapping("/show/statistics")
    public R showStatistics(@RequestBody @Validated StatisticsSearchCondition statisticsSearchCondition){
        String type = statisticsSearchCondition.getType();
        String begin = statisticsSearchCondition.getBegin();
        String end = statisticsSearchCondition.getEnd();
        log.info("Rest to get {} statistics data from {} to {}",type,begin,end);

        Map<String, List> map = statisticsDailyService.showStatistics(type,begin,end);

        return R.ok().data("statisticsMap",map);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class StatisticsSearchCondition{
        // 展示哪个数据
        @NotBlank
        private String type;
        // 开始
        @NotBlank
        private String begin;
        // 结束
        @NotBlank
        private String end;
    }

}

