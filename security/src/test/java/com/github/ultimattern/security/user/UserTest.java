package com.github.ultimattern.security.user;

import com.github.ultimattern.security.role.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Salman
 * @since 2/11/2025
 */
@SpringBootTest
@TestPropertySource(locations = "file:./.env")
class UserTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    void createUser() {
        User user = new User();
        user.setUsername("test_username");
        user.setPassword(passwordEncoder.encode("test_password"));
        user.setRole(Role.ADMIN);
        user.setIsActive(false);
        user.setIsLocked(false);
        User saved = userRepository.save(user);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("test_username", saved.getUsername());
    }
}