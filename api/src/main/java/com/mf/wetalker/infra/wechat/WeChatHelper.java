package com.mf.wetalker.infra.wechat;

import com.mf.wetalker.infra.json.MfObjectMapper;
import com.mf.wetalker.infra.auth.WechatAuthObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeChatHelper {

    @Value("${wetalk.wechat.app_id}")
    public String appId;
    @Value("${wetalk.wechat.app_secret}")
    public String appSecret;
    public static final String AUTHORIZATION_CODE = "authorization_code";
    private final WeChatFeignClient weChatFeignClient;

    public WechatAuthObject login(String code) {
        String response = weChatFeignClient.login(appId, appSecret, code, AUTHORIZATION_CODE);
        WechatAuthObject authObject = MfObjectMapper.getObjectFromJsonString(response, WechatAuthObject.class);
        return authObject;
    }
}