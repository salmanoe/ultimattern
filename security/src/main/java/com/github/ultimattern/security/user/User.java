package com.github.ultimattern.security.user;

import com.github.ultimattern.security.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static org.hibernate.annotations.UuidGenerator.Style.TIME;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/11/2025
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = "public")
public class User implements UserDetails, Serializable {

    @Id
    @UuidGenerator(style = TIME)
    private UUID id;

    @Size(max = 100)
    @NotBlank
    @Column(length = 100, nullable = false, unique = true)
    private String username;

    @Size(max = 100)
    @NotBlank
    @Column(length = 100, nullable = false)
    private String password;

    @Size(max = 50)
    @NotNull
    @Column(length = 50, nullable = false)
    @Enumerated(STRING)
    private Role role;

    private Boolean isLocked;

    private Boolean isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
