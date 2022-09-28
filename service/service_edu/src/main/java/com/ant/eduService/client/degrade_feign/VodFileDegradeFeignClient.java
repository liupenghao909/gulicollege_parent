package com.ant.eduService.client.degrade_feign;

import com.ant.commonutils.R;
import com.ant.eduService.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * openfeign中的方法执行错误时，对应执行的兜底方法
 */
@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R deleteVideoById(String videoId) {
        return R.error().message("删除视频出错");
    }

    @Override
    public R deleteVideoBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错");
    }
}
