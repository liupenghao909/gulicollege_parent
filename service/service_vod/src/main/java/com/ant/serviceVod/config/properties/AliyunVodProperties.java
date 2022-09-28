package com.ant.serviceVod.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "aliyun.vod.file")
@RefreshScope
@Data
public class AliyunVodProperties {

    private String keyid;
    private String keysecret;

}
