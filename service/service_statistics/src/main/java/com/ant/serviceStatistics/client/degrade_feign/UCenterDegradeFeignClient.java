package com.ant.serviceStatistics.client.degrade_feign;

import com.ant.commonutils.R;
import com.ant.serviceStatistics.client.UCenterClient;
import org.springframework.stereotype.Component;

@Component
public class UCenterDegradeFeignClient implements UCenterClient {

    @Override
    public R getMemberRegisterNum(String day) {
        return R.error().data("num",0);
    }
}
