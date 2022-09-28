package com.ant.eduService.client.degrade_feign;

import com.ant.eduService.client.OrderClient;
import org.springframework.stereotype.Component;

@Component
public class OrderDegradeFeignClient implements OrderClient {
    @Override
    public boolean judgeCourseIsBuy(String courseId, String memberId) {
        // 默认返回false
        return false;
    }
}
