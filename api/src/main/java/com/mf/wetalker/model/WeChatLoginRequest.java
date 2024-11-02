package com.mf.wetalker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeChatLoginRequest {

    private String code;
    private String avatarUrl;
    private String nickName;

}
