package com.github.ultimattern.security.config.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.ultimattern.security.config.redis.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.github.ultimattern.security.config.jwt.JWTUtil.getDecodedJWT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/15/2025
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JWTLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null) {
            String username;
            try {
                username = getDecodedJWT(authorizationHeader).getSubject();
            } catch (TokenExpiredException e) {
                username = null;
            }
            String token = authorizationHeader.substring("Bearer ".length());
            if (!RedisUtil.isRevokedToken(token)) {
                if (username != null)
                    log.info("Logout - {}", username);

                RedisUtil.revokeToken(token, Duration.ofHours(1));
                SecurityContextHolder.clearContext();
            }
        }
    }
}
