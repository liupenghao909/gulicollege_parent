package com.ant.eduService.client;

import com.ant.eduService.client.degrade_feign.OrderDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order",fallback = OrderDegradeFeignClient.class)
public interface OrderClient {
    @GetMapping("/serviceOrder/order/judge/order/isPay/{courseId}/{memberId}")
    public boolean judgeCourseIsBuy(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId);
}
