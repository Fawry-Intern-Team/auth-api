package com.example.auth_service.controller;

import com.example.auth_service.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class RefreshTokenController {
    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "Refresh-Token", required = false) String refreshToken,
            HttpServletResponse response) {
        String newAccessToken = refreshTokenService.refresh(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("Access-Token", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(15 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", accessCookie.toString());

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
