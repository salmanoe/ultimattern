package com.github.ultimattern.security.role;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.github.ultimattern.security.config.ResponseHandler.generateResponse;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/20/2025
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        List<RoleDTO> dtos = roleService.findAll();
        return generateResponse(OK.getReasonPhrase(), OK, dtos);
    }
}
