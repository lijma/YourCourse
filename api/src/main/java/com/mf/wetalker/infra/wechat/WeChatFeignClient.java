package com.mf.wetalker.infra.wechat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weChatFeignClient", url = "https://api.weixin.qq.com/sns", configuration = WechatFeignConfig.class)
@Service
public interface WeChatFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/jscode2session", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    String login(@RequestParam("appid") String appId,
                           @RequestParam("secret") String secret,
                           @RequestParam("js_code") String jsCode,
                           @RequestParam("grant_type") String grant_type);

}