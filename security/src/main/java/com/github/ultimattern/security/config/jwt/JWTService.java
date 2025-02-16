package com.github.ultimattern.security.config.jwt;

import com.auth0.jwt.JWT;
import com.github.ultimattern.security.config.redis.RedisUtil;
import com.github.ultimattern.security.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Collectors;

import static com.github.ultimattern.security.config.jwt.JWTUtil.*;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/13/2025
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JWTService implements TokenService {

    @Value("${application.security.jwt.expiration.access}")
    private long accessExpiration;

    @Value("${application.security.jwt.expiration.refresh}")
    private long refreshExpiration;

    @Override
    public String generateAccessToken(HttpServletRequest request, UserDetails userDetails) {
        log.info("Generate JWT access token");

        return buildToken(request, userDetails, accessExpiration);
    }

    @Override
    public String generateRefreshToken(HttpServletRequest request, UserDetails userDetails) {
        log.info("Generate JWT refresh token");

        return buildToken(request, userDetails, refreshExpiration);
    }

    private static String buildToken(HttpServletRequest request, UserDetails userDetails, long expiration) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuer(request.getRequestURL().toString())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(getAlgorithm());
    }

    @Override
    public boolean isTokenValid(String authorizationHeader, UserDetails userDetails) {
        log.info("Check token validity");

        boolean validSubject = getDecodedJWT(authorizationHeader).getSubject().equals(userDetails.getUsername());
        boolean tokenExpired = isTokenExpired(authorizationHeader);
        boolean tokenRevoked = isTokenRevoked(authorizationHeader);
        return validSubject && !tokenExpired && !tokenRevoked;
    }

    private boolean isTokenExpired(String authorizationHeader) {
        return getDecodedJWT(authorizationHeader).getExpiresAt().before(new Date());
    }

    private boolean isTokenRevoked(String authorizationHeader) {
        String token = getToken(authorizationHeader);
        return RedisUtil.isRevokedToken(token);
    }

    @Override
    public void revokeToken(String token) {
        log.info("Revoke token");

        RedisUtil.revokeToken(token, Duration.ofHours(1));
    }
}
