package com.ant.serviceOSS.controller;

import com.ant.commonutils.R;
import com.ant.serviceOSS.service.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/edu/oss/file")
@CrossOrigin
public class OssController {
    private final Logger log = LoggerFactory.getLogger(OssController.class);

    private final OssService ossService;

    public OssController(OssService ossService) {
        this.ossService = ossService;
    }

    @PostMapping("/upload/avatar")
    public R uploadTeacherAvatar(MultipartFile file){
        log.info("Rest to upload teacher avatar");

        String url = ossService.uploadAvatar(file);
        return R.ok().data("url",url);
    }

}
