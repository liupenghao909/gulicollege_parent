package com.ant.serviceVod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadVideoToAliyun(MultipartFile file);

    Boolean deleteVideoById(String videoId);

    void deleteVideoBatch(List<String> videoIdList);

    String getPlayAuth(String videoSourceId);
}
