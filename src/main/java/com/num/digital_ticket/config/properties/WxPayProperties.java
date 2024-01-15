package com.num.digital_ticket.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
    private String gateway;
    private String appid;
    private String mchkey;
    private String mchid;
    private String type;
    private String notifyUrl;
}
