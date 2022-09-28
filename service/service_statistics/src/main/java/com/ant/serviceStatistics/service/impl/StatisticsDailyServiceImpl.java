package com.ant.serviceStatistics.service.impl;

import com.ant.commonutils.R;
import com.ant.commonutils.RandomUtil;
import com.ant.serviceStatistics.client.UCenterClient;
import com.ant.serviceStatistics.entity.StatisticsDaily;
import com.ant.serviceStatistics.enumeration.StatisticsSearchTypeEnum;
import com.ant.serviceStatistics.mapper.StatisticsDailyMapper;
import com.ant.serviceStatistics.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author ant
 * @since 2022-09-24
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    private final UCenterClient uCenterClient;

    public StatisticsDailyServiceImpl(UCenterClient uCenterClient) {
        this.uCenterClient = uCenterClient;
    }

    @Override
    public void createStatisticsByDate(String day) {
        R r = uCenterClient.getMemberRegisterNum(day);
        Integer num = (Integer) r.getData().get("num");

        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated",day);
        int delete = baseMapper.delete(queryWrapper);

        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setDateCalculated(day);
        statisticsDaily.setCourseNum(RandomUtils.nextInt(100,300));
        statisticsDaily.setLoginNum(RandomUtils.nextInt(100,300));
        statisticsDaily.setVideoViewNum(RandomUtils.nextInt(100,300));
        statisticsDaily.setRegisterNum(num);

        baseMapper.insert(statisticsDaily);

    }

    @Override
    public Map<String, List> showStatistics(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date_calculated",begin,end);
        queryWrapper.select("date_calculated",type);
        queryWrapper.orderByAsc("date_calculated");

        List<StatisticsDaily> statisticsDailyList = baseMapper.selectList(queryWrapper);

        ArrayList<String> dateCalculatedList = new ArrayList<>();
        ArrayList<Integer> dataNumList = new ArrayList<>();


        String finalType = type.toUpperCase();;
        statisticsDailyList.stream().forEach(statisticsDaily -> {
            dateCalculatedList.add(statisticsDaily.getDateCalculated());

            Class aClass = statisticsDaily.getClass();
            try {

                String filedName = StatisticsSearchTypeEnum.valueOf(finalType).getFiled();
                Field field = aClass.getDeclaredField(filedName);
                field.setAccessible(true);
                Integer num = (Integer) field.get(statisticsDaily);
                dataNumList.add(num);
            }catch (Exception e){
                e.printStackTrace();
                dataNumList.add(0);
            }
        });

        return new HashMap<String,List>(){
            {
                put("dateCalculatedList", dateCalculatedList);
                put("dataNumList", dataNumList);
            }
        };


    }
}
