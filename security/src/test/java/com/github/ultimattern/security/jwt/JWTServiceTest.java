package com.github.ultimattern.security.jwt;

import com.github.ultimattern.security.role.Role;
import com.github.ultimattern.security.user.User;
import com.github.ultimattern.security.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

/**
 * @author Salman
 * @since 2/13/2025
 */
@TestPropertySource(locations = "file:./.env")
@SpringBootTest
class JWTServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JWTService jwtService;

    @Autowired
    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        Optional<User> testUsername = userRepository.findByUsername("test_username");
        if (testUsername.isEmpty()) {
            User user = new User();
            user.setUsername("test_username");
            user.setPassword(passwordEncoder.encode("test_password"));
            user.setRole(Role.ADMIN);
            user.setIsActive(false);
            user.setIsLocked(false);
            userRepository.save(user);
        }
    }

    @Test
    void generateAccessToken() {
        Optional<User> testUsername = userRepository.findByUsername("test_username");
        testUsername.ifPresent(user -> {
            String accessToken = jwtService.generateAccessToken(request, user);
            Assertions.assertNotNull(accessToken);
        });
    }

    @Test
    void generateRefreshToken() {
        Optional<User> testUsername = userRepository.findByUsername("test_username");
        testUsername.ifPresent(user -> {
            String refreshToken = jwtService.generateRefreshToken(request, user);
            Assertions.assertNotNull(refreshToken);
        });
    }

    @Test
    void validToken() {
        Optional<User> testUsername = userRepository.findByUsername("test_username");
        testUsername.ifPresent(user -> {
            String accessToken = jwtService.generateAccessToken(request, user);
            boolean tokenValid = jwtService.isTokenValid("Bearer " + accessToken, user);
            Assertions.assertTrue(tokenValid);
        });
    }

    @Test
    void invalidLogoutToken() {
        Optional<User> testUsername = userRepository.findByUsername("test_username");
        testUsername.ifPresent(user -> {
            String accessToken = jwtService.generateAccessToken(request, user);
            jwtService.revokeToken(accessToken);
            boolean tokenValid = jwtService.isTokenValid("Bearer " + accessToken, user);
            Assertions.assertFalse(tokenValid);
        });
    }
}