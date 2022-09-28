package com.ant.serviceOrder.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "weixin.pay")
@RefreshScope
@Data
public class WechatPaymentQRProperties {

    private String appid;

    private String partner;

    private String partnerkey;

    private String notifyurl;

}
