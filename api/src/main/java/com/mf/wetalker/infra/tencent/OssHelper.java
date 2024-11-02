package com.mf.wetalker.infra.tencent;

import com.qcloud.cos.transfer.TransferManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
@Slf4j
public class OssHelper {
    private final TransferManager transferManager;
    private final CosProperty config;

    public String upload(File file) {
        try {
            log.debug("[tencent] upload {} successfully", file.getName());
            String key = config.getPath() + file.getName();
            transferManager.upload(config.getBucketName(), key, file);

            return new StringBuilder("https://")
                    .append(config.getBucketName())
                    .append(".cos.")
                    .append(config.getRegion())
                    .append(".myqcloud.com/")
                    .append(key)
                    .toString();
        } catch (Exception e) {
            log.error("[tencent]failed to upload: {} ", file, e);
            return null;
        }
    }

}
