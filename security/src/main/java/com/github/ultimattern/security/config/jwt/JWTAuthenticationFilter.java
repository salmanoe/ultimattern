package com.github.ultimattern.security.config.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.ultimattern.security.config.ResponseHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.github.ultimattern.security.config.CommonMessage.TOKEN_EXPIRED;
import static com.github.ultimattern.security.config.jwt.JWTUtil.getDecodedJWT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/16/2025
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null) {
            try {
                DecodedJWT decodedJWT = getDecodedJWT(authorizationHeader);
                if (decodedJWT.getSubject() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
                    boolean tokenValid = jwtService.isTokenValid(authorizationHeader, userDetails);
                    if (tokenValid) {
                        var authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                filterChain.doFilter(request, response);
            } catch (TokenExpiredException e) {
                log.error(e.getMessage());

                ResponseHandler.ofException(response, TOKEN_EXPIRED, UNAUTHORIZED);
            } catch (Exception e) {
                log.error(e.getMessage());

                ResponseHandler.ofException(response, INTERNAL_SERVER_ERROR.getReasonPhrase(), INTERNAL_SERVER_ERROR);
            }
        }
    }
}
