package com.ant.eduService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import java.util.LinkedList;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ant"})
@EnableDiscoveryClient   // 注册到nacos
@EnableFeignClients // 开启feign服务
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class,args);
    }
}
