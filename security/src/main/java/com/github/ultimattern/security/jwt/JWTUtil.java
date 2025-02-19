package com.github.ultimattern.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/13/2025
 */
@Component
public class JWTUtil {

    private static String SECRET_KEY;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.secret-key}")
    public void setSecretKey(String secretKey) {
        JWTUtil.SECRET_KEY = secretKey;
    }

    public static DecodedJWT getDecodedJWT(String authorizationHeader) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(getToken(authorizationHeader));
    }

    public static String getToken(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY.getBytes());
    }
}
