package com.github.ultimattern.security.config;

import com.github.ultimattern.security.jwt.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/16/2025
 */
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    private final JWTAuthenticationFilter authenticationFilter;

    private final LogoutHandler logoutHandler;

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(header -> {
                    header.xssProtection(Customizer.withDefaults());
                    header.contentSecurityPolicy(h ->
                            h.policyDirectives("script-src 'self'"));
                })
                .sessionManagement(management ->
                        management.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(request ->
                        request.anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e ->
                        e.accessDeniedHandler(accessDeniedHandler()))
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.addLogoutHandler(logoutHandler);
                    logout.logoutSuccessHandler(
                            (request, response, authentication) ->
                                    SecurityContextHolder.clearContext());
                });
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
