package com.ant.serviceOrder.client.degrade_feign;

import com.ant.commonutils.vo.MemberBaseInfo;
import com.ant.serviceOrder.client.UCenterClient;
import org.springframework.stereotype.Component;

@Component
public class UCenterDegradeFeignClient implements UCenterClient {

    @Override
    public MemberBaseInfo getMemberBaseInfo(String memberId) {
        return null;
    }
}
