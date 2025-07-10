package com.example.auth_service.controller;

import com.example.auth_service.dto.RefreshTokenDTO;
import com.example.auth_service.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class RefreshTokenController {
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO refreshToken) {
        String newAccessToken = refreshTokenService.refresh(refreshToken.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("Access-Token", newAccessToken));
    }
}
