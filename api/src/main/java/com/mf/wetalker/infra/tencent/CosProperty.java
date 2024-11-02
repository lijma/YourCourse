package com.mf.wetalker.infra.tencent;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wetalk.oss")
@Data
public class CosProperty {

    private String secretId;
    private String secretKey;
    private String region;
    private String bucketName;
    private String path;

}