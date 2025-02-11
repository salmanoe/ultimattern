package com.github.ultimattern.security.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.github.ultimattern.security.role.Permission.*;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/11/2025
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN(Set.of(ADMIN_CREATE, ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE));

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = new ArrayList<>(getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
