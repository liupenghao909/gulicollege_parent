package com.ant.serviceOrder.client;


import com.ant.commonutils.vo.WebCourseVO;
import com.ant.serviceOrder.client.degrade_feign.EduDegradeFeignClient;
import com.ant.serviceOrder.client.degrade_feign.UCenterDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-edu",fallback = EduDegradeFeignClient.class)
@Component
public interface EduClient {
    @GetMapping("/eduService/frontCourse/order/get/courseInfo/{courseId}")
    public WebCourseVO orderGetCourseInfo(@PathVariable("courseId") String courseId);
}
