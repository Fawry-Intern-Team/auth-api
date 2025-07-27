package com.example.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint - no authentication required");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/protected")
    public ResponseEntity<Map<String, Object>> protectedEndpoint(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();
        
        if (oauth2User != null) {
            response.put("message", "This is a protected endpoint - authentication required");
            response.put("status", "success");
            response.put("user", oauth2User.getName());
            response.put("email", oauth2User.getAttribute("email"));
        } else {
            response.put("message", "Authentication required");
            response.put("status", "error");
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "auth-service");
        response.put("oauth2", "enabled");
        return ResponseEntity.ok(response);
    }
} 