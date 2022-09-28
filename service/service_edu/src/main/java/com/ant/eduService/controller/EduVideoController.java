package com.ant.eduService.controller;


import com.ant.commonutils.R;
import com.ant.eduService.client.VodClient;
import com.ant.eduService.entity.EduVideo;
import com.ant.eduService.service.EduVideoService;
import com.ant.servicebase.exceptionhandler.GuliException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author ant
 * @since 2022-08-31
 */
@RestController
@RequestMapping("/eduService/eduVideo")

public class EduVideoController {
    private final Logger log = LoggerFactory.getLogger(EduVideoController.class);

    private final EduVideoService eduVideoService;

    private final VodClient vodClient;

    public EduVideoController(EduVideoService eduVideoService, VodClient vodClient) {
        this.eduVideoService = eduVideoService;
        this.vodClient = vodClient;
    }

    /**
     * 保存小节信息
     * @param eduVideo
     * @return
     */
    @PostMapping("/add/video")
    public R addVideo(@RequestBody EduVideo eduVideo){
        boolean save = eduVideoService.save(eduVideo);

        if(!save){
            throw new GuliException(20001,"添加小节信息失败");
        }

        return R.ok();
    }

    /**
     * 根据videoID删除video,删除小节之前先将小节对应的视频从阿里云视频点播中删除
     * @param videoId
     * @return
     */
    @GetMapping("/delete/video/{videoId}")
    public R deleteVideoById(@PathVariable("videoId") String videoId){
        log.info("Rest to delete video by id");

        EduVideo eduVideo = eduVideoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();

        if(!StringUtils.isBlank(videoSourceId)) {
            R result = vodClient.deleteVideoById(videoSourceId);
            throw new GuliException(20001,result.getMessage());
        }

        boolean b = eduVideoService.removeById(videoId);

        if(!b){
            throw new GuliException(20001,"删除小节信息失败");
        }

        return R.ok();
    }

    /**
     * 更新video信息
     * @param eduVideo
     * @return
     */
    @PostMapping("/update/video")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        boolean b = eduVideoService.updateById(eduVideo);

        if(!b){
            throw new GuliException(20001,"更新小节信息失败");
        }

        return R.ok();
    }

    /**
     * 查询video信息
     * @param videoId
     * @return
     */
    @GetMapping("/get/video/{videoId}")
    public R getVideoById(@PathVariable("videoId") String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);

        return R.ok().data("eduVideo",eduVideo);
    }
}

