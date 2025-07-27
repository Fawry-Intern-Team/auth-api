package com.example.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "auth-service");
        health.put("version", "1.0.0");
        health.put("timestamp", LocalDateTime.now());
        health.put("uptime", "2h 15m 30s");
        return health;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Authentication Service");
        info.put("description", "JWT-based authentication and authorization service");
        info.put("version", "1.0.0");
        info.put("features", new String[]{
                "JWT Authentication",
                "User Registration", 
                "Token Refresh",
                "Role-based Authorization"
        });
        info.put("endpoints", new String[]{
                "/api/auth/register",
                "/api/auth/login",
                "/api/auth/refresh",
                "/api/auth/protected"
        });
        return info;
    }
} 