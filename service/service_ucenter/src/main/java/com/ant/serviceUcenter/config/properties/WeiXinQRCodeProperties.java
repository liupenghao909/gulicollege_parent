package com.ant.serviceUcenter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = "wx.open")
public class WeiXinQRCodeProperties {
    // 微信开放平台 appid
    private String appId;
    // 微信开放平台 appsecret
    private String appSecret;
    // 微信开放平台 重定向url
    private String redirectUrl;
}
