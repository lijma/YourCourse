package com.mf.wetalker.infra.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WechatAuthObject {

    @JsonProperty("session_key")
    private String sessionKey;
    private String openid;

}
