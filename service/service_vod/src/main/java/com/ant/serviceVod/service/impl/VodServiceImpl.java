package com.ant.serviceVod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.ant.serviceVod.config.properties.AliyunVodProperties;
import com.ant.serviceVod.service.VodService;
import com.ant.serviceVod.utils.InitVod;
import com.ant.servicebase.exceptionhandler.GuliException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(AliyunVodProperties.class)
@Service
public class VodServiceImpl implements VodService {
    private final Logger log = LoggerFactory.getLogger(VodServiceImpl.class);

    private final AliyunVodProperties vodProperties;

    public VodServiceImpl(AliyunVodProperties vodProperties) {
        this.vodProperties = vodProperties;
    }

    @Override
    public String uploadVideoToAliyun(MultipartFile file) {
        try {
            String accessKeyId = vodProperties.getKeyid();
            String accessKeySecret = vodProperties.getKeysecret();

            String fileName = file.getOriginalFilename();
            // 规定title为视频文件名去掉后缀
            String title = fileName.substring(0, fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();


            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
            /* 点播服务接入点 */
            request.setApiRegionId("cn-beijing");
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId;  //请求视频点播服务的请求ID
            videoId = response.getVideoId();

            return videoId;
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"guli 上传视频失败");
        }
    }

    @Override
    public Boolean deleteVideoById(String videoId) {
        try {
            DefaultAcsClient vodClient = InitVod.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
            DeleteVideoRequest deleteVideoRequest = new DeleteVideoRequest();
            deleteVideoRequest.setVideoIds(videoId);

            DeleteVideoResponse deleteVideoResponse = vodClient.getAcsResponse(deleteVideoRequest);

            log.info("the response of the delete request:{}",deleteVideoResponse.toString());
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除小节视频失败");
        }

        return true;
    }

    /**
     * 删除多个video
     * @param videoIdList
     */
    @Override
    public void deleteVideoBatch(List<String> videoIdList) {
        try {
            DefaultAcsClient vodClient = InitVod.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
            DeleteVideoRequest deleteVideoRequest = new DeleteVideoRequest();

            String videoIds = videoIdList.stream().collect(Collectors.joining(","));
            deleteVideoRequest.setVideoIds(videoIds);

            DeleteVideoResponse deleteVideoResponse = vodClient.getAcsResponse(deleteVideoRequest);

            log.info("the response of the delete request:{}",deleteVideoResponse.toString());
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除多个小节视频失败");
        }

    }

    // 根据视频源id获取视频播放凭证
    @Override
    public String getPlayAuth(String videoSourceId) {
        try{
            DefaultAcsClient vodClient = InitVod.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());

            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoSourceId);
            GetVideoPlayAuthResponse response = vodClient.getAcsResponse(request);

            return response.getPlayAuth();
        }catch (Exception e){
            throw new GuliException(20001,e.toString());
        }

    }
}
