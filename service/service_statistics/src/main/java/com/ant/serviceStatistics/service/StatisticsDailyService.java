package com.ant.serviceStatistics.service;

import com.ant.serviceStatistics.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author ant
 * @since 2022-09-24
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void createStatisticsByDate(String day);

    Map<String, List> showStatistics(String type, String begin, String end);
}
