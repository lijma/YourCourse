package com.mf.wetalker.infra.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile(value="!debug")
public class WeTalkPermissionInterceptor implements HandlerInterceptor {

    private final WeChatJwtTokenHolder weChatJwtTokenHolder;

    @Override
    public void postHandle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        LogRequired guard = null;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            guard = handlerMethod.getMethodAnnotation(LogRequired.class);
        }

        if (guard != null && guard.enable()) {
            try {
                validateToken(request, guard);
            }catch (HttpClientErrorException exception){
                response.getWriter().write(exception.getMessage());
                response.setStatus(exception.getStatusCode().value());
                return false;
            }
        }

        return true;
    }

    private void validateToken(HttpServletRequest request, LogRequired guard) throws HttpClientErrorException {
        String tokenHeader = request.getHeader("Authorization");
        if (Strings.isBlank(tokenHeader) || tokenHeader.length() <= 7) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "token not exists, pls login first");
        }

        String jwtToken = tokenHeader.substring(7, tokenHeader.length());
        BigInteger userId = weChatJwtTokenHolder.getUserId(jwtToken);

        if (userId == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "token invalid, pls login first");
        }

        if (guard.role() == Role.ADMIN) {
            //TODO: validate admin dashboard here
        }

        UserContextHolder.setUserId(userId);
    }

}
