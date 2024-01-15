package com.num.digital_ticket.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wx.login")
public class WxLoginProperties {
    private String appid;
    private String appSecret;
    private String redirectUrl;
}
