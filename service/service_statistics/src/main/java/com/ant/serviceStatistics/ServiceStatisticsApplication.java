package com.ant.serviceStatistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan({"com.ant"})
@EnableDiscoveryClient // 注册到nacos
@EnableFeignClients  // 激活feign客户端，从而可以调用nacos中其他微服务
@MapperScan("com.ant.serviceStatistics.mapper")  // 扫描maybatis-plus mapper文件的地址
@EnableScheduling // 开启定时任务
public class ServiceStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceStatisticsApplication.class, args);
    }

}
