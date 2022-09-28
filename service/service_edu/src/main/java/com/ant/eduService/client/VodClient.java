package com.ant.eduService.client;

import com.ant.commonutils.R;
import com.ant.eduService.client.degrade_feign.VodFileDegradeFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {
    @GetMapping("/eduVod/video/delete/AliyunVideo/{videoId}")
    public R deleteVideoById(@PathVariable("videoId")String videoId);

    @GetMapping("/eduVod/video/delete/AliyunVideo/list")
    public R deleteVideoBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
