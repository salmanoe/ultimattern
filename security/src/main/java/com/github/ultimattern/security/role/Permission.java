package com.github.ultimattern.security.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/11/2025
 */
@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_CREATE("admin:create"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete");

    private final String permission;
}
