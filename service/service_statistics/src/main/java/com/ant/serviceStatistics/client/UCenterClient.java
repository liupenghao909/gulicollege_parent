package com.ant.serviceStatistics.client;

import com.ant.commonutils.R;
import com.ant.serviceStatistics.client.degrade_feign.UCenterDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter",fallback = UCenterDegradeFeignClient.class)
public interface UCenterClient {
    @GetMapping("/serviceUcenter/ucenterMember/get/member/registerNum/{day}")
    public R getMemberRegisterNum(@PathVariable("day") String day);
}
