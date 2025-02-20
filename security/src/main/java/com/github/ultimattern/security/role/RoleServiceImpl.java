package com.github.ultimattern.security.role;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/20/2025
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Override
    public List<RoleDTO> findAll() {
        log.info("Get all roles");

        return Arrays.stream(Role.values()).toList()
                .stream()
                .map(role -> new RoleDTO(role.getLabel(), role.name()))
                .toList();
    }
}
