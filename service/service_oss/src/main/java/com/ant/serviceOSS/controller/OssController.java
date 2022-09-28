package com.ant.serviceOSS.controller;

import com.ant.commonutils.R;
import com.ant.serviceOSS.service.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduOss/fileoss")

public class OssController {
    private final Logger log = LoggerFactory.getLogger(OssController.class);

    private final OssService ossService;

    public OssController(OssService ossService) {
        this.ossService = ossService;
    }

    @PostMapping("/upload/avatar")
    public R uploadTeacherAvatar(MultipartFile file, @RequestParam(required = false,name = "host") String host){
        log.info("Rest to upload teacher avatar");

        String url = ossService.uploadAvatar(file, host);
        return R.ok().data("url",url);
    }

}
