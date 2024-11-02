package com.mf.wetalker.infra.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@Slf4j
@Profile(value = "debug")
public class DebugInterceptor extends WeTalkPermissionInterceptor {

    public DebugInterceptor(WeChatJwtTokenHolder weChatJwtTokenHolder) {
        super(weChatJwtTokenHolder);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {

        UserContextHolder.setUserId(new BigInteger("1623644529674530818"));

        return true;
    }


}
