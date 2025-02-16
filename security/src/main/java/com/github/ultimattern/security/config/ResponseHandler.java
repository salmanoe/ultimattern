package com.github.ultimattern.security.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Salman
 * @version 0.0.1
 * @since 2/13/2025
 */
public class ResponseHandler {

    public static ResponseEntity<Object> generateResponse(String message, HttpStatusCode status, Object response) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("message", message);
        metadata.put("code", status.value());
        if (response != null)
            map.put("response", response);
        map.put("metadata", metadata);
        return new ResponseEntity<>(map, status);
    }

    public static void ofException(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        ResponseEntity<Object> entity = generateResponse(message, status, null);
        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(Objects.requireNonNull(entity.getBody()).toString());
    }
}
