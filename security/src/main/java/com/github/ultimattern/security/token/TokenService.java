package com.github.ultimattern.security.token;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/13/2025
 */
public interface TokenService {

    String generateAccessToken(HttpServletRequest request, UserDetails userDetails);
    String generateRefreshToken(HttpServletRequest request, UserDetails userDetails);
    boolean isTokenValid(String authorizationHeader, UserDetails userDetails);
    void revokeToken(String token);
}
