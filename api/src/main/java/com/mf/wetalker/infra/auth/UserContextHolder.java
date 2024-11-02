package com.mf.wetalker.infra.auth;

import org.slf4j.MDC;

import java.math.BigInteger;

public class UserContextHolder {

    private static String USER_KEY = "userId";

    public static BigInteger getUserId() {
        String userIdString = MDC.get(UserContextHolder.USER_KEY);
        return new BigInteger(userIdString);
    }

    public static void setUserId(BigInteger userId) {
        MDC.put(UserContextHolder.USER_KEY, userId.toString());
    }


    private UserContextHolder() {
    }
}
