package com.ant.serviceVod.controller;

import com.ant.commonutils.R;
import com.ant.serviceVod.service.VodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduVod/video")

public class VodController {
    private final Logger log = LoggerFactory.getLogger(VodController.class);

    private final VodService vodService;

    public VodController(VodService vodService) {
        this.vodService = vodService;
    }

    @PostMapping("/upload")
    public R uploadVideoToAliyun(MultipartFile file){
        log.info("Rest to upload video to Aliyun");

        String videoId = vodService.uploadVideoToAliyun(file);

        return R.ok().data("videoId",videoId);

    }

    @GetMapping("/delete/AliyunVideo/{videoId}")
    public R deleteVideoById(@PathVariable("videoId")String videoId){
        log.info("Rest to delete video by videoId");

        Boolean isSuccess = vodService.deleteVideoById(videoId);

        if(isSuccess){
            return R.ok();
        }

        return R.error();
    }

    @GetMapping("/delete/AliyunVideo/list")
    public R deleteVideoBatch(@RequestParam("videoIdList") List<String> videoIdList){
        log.info("Rest to delete video in videoIdList:{}",videoIdList);

        vodService.deleteVideoBatch(videoIdList);

        return R.ok();
    }

    /**
     * 根据视频源id获取视频播放凭证
     * @param videoSourceId
     * @return
     */
    @GetMapping("/get/playAuth/{videoSourceId}")
    public R getPlayAuth(@PathVariable("videoSourceId") String videoSourceId){
        String playAuth = vodService.getPlayAuth(videoSourceId);

        return R.ok().data("playAuth",playAuth);
    }
}
