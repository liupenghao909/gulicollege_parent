package com.ant.serviceOrder.client.degrade_feign;

import com.ant.commonutils.vo.WebCourseVO;
import com.ant.serviceOrder.client.EduClient;
import org.springframework.stereotype.Component;

@Component
public class EduDegradeFeignClient implements EduClient {
    @Override
    public WebCourseVO orderGetCourseInfo(String courseId) {
        return null;
    }
}
