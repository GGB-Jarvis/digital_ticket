package com.num.digital_ticket.config;

import com.num.digital_ticket.config.properties.WxPayProperties;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfig {
    private final WxPayProperties wxPayProperties;

    public PayConfig(WxPayProperties wxPayProperties) {
        this.wxPayProperties = wxPayProperties;
    }

//    @Bean
//    public Config wxPayConfig() {
//        // 初始化商户配置
//        return new RSAAutoCertificateConfig.Builder()
//                .merchantId(wxPayProperties.getMchid())
//                // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
////                        .privateKeyFromPath(privateKeyPath)
////                        .merchantSerialNumber(merchantSerialNumber)
//                .apiV3Key(wxPayProperties.getMchkey())
//                .build();
//    }
}
