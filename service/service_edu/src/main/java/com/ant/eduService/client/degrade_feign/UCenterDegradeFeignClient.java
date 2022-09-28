package com.ant.eduService.client.degrade_feign;

import com.ant.commonutils.R;
import com.ant.commonutils.vo.MemberBaseInfo;
import com.ant.eduService.client.UCenterClient;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UCenterDegradeFeignClient implements UCenterClient {

    @Override
    public MemberBaseInfo getMemberBaseInfo(String memberId) {
        return null;
    }
}
