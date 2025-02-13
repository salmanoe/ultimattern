package com.github.ultimattern.security.config;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

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
}
