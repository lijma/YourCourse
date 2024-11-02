package com.mf.wetalker.infra.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigInteger;

@Component
@Slf4j
public class WeChatJwtTokenHolder {

    @Value("${wetalk.auth.secret}")
    public String secret;
    @Value("${wetalk.auth.issuer}")
    public String issuer;
    @Value("${wetalk.auth.expire_day}")
    public Integer expireDay = 30;

    public static final String USER_ID_KEY = "userId";

    public String generate(BigInteger userId) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        return JWT.create()
                .withIssuer(issuer)
                .withClaim(USER_ID_KEY, userId.toString())
                .withExpiresAt(LocalDate.now().plusDays(expireDay).toDate())
                .sign(algorithm);

    }

    public BigInteger getUserId(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secret);

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            log.debug("Verify JWT token success.");

            Claim claim = decodedJWT.getClaims().get(USER_ID_KEY);
            BigInteger userId = new BigInteger(claim.asString());
            return userId;
        } catch (JWTVerificationException ex) {
            log.error("failed to parse userId from jwt token");
            return null;
        }
    }


    public static void main(String args[]) {
        WeChatJwtTokenHolder generator = new WeChatJwtTokenHolder();
        BigInteger userId1 = BigInteger.valueOf(1000L);
        String token = generator.generate(userId1);
        BigInteger userId = generator.getUserId(token);
        Assert.isTrue(userId1.compareTo(userId) == 1, "jwt验证");
    }
}
