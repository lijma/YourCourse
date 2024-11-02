package com.mf.wetalker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mf.wetalker.infra.tencent.CosProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CosConfig {

    @JsonProperty("secret_id")
    private String secretId;
    @JsonProperty("secret_key")
    private String secretKey;
    private String region;
    @JsonProperty("bucket_name")
    private String bucketName;
    private String path;

    public CosConfig(CosProperty cosProperty) {
        this.bucketName = cosProperty.getBucketName();
        this.secretId = cosProperty.getSecretId();
        this.secretKey = cosProperty.getSecretKey();
        this.region = cosProperty.getRegion();
        this.path = cosProperty.getPath();
    }

}