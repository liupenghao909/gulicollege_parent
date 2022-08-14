package com.ant.serviceOSS.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "aliyun.oss.file")
@RefreshScope
@Data
public class AliyunOSSProperties {

    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;

}
