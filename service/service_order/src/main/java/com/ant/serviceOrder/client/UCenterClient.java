package com.ant.serviceOrder.client;


import com.ant.commonutils.vo.MemberBaseInfo;

import com.ant.serviceOrder.client.degrade_feign.UCenterDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-ucenter",fallback = UCenterDegradeFeignClient.class)
@Component
public interface UCenterClient {

    // 根据token获取用户信息
    @GetMapping("/serviceUcenter/ucenterMember/get/member/baseInfo/{memberId}")
    public MemberBaseInfo getMemberBaseInfo(@PathVariable("memberId") String memberId);
}
