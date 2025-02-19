package com.github.ultimattern.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

import static java.lang.Boolean.TRUE;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/15/2025
 */
@Component
public class RedisUtil {

    private static String REVOKED_TOKEN_DIR;

    private static RedisTemplate<String, Object> REDIS_TEMPLATE;

    @Value("${application.security.redis.token.revoke-directory}")
    private String revokedTokenDirectory;

    @Value("${application.security.redis.token.revoke-directory}")
    public void setRevokedTokenDirectory(String revokedTokenDirectory) {
        RedisUtil.REVOKED_TOKEN_DIR = revokedTokenDirectory;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.REDIS_TEMPLATE = redisTemplate;
    }

    public static void revokeToken(String token, Duration ttl) {
        REDIS_TEMPLATE.opsForValue().set(RedisUtil.revokedTokenKey(token), TRUE.toString(), ttl);
    }

    public static boolean isRevokedToken(String token) {
        return Boolean.parseBoolean(Objects
                .requireNonNullElse(REDIS_TEMPLATE.opsForValue().get(RedisUtil.revokedTokenKey(token)), false)
                .toString());
    }

    private static String revokedTokenKey(String token) {
        return REVOKED_TOKEN_DIR + token;
    }
}
