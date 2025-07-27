package com.example.auth_service.controller;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO request,
                                      HttpServletResponse response) {
        logger.info("Register endpoint called for email: {}", request.getEmail());

        var tokens = userService.register(request);
        String refreshToken = tokens.get("Refresh-Token");

        ResponseCookie refreshCookie = ResponseCookie.from("Refresh-Token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());

        ResponseCookie accessCookie = ResponseCookie.from("Access-Token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(15 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", accessCookie.toString());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO request,
                                   HttpServletResponse response) {
        logger.info("Login endpoint called for email: {}", request.getEmail());

        var tokens = userService.login(request);
        String refreshToken = tokens.get("Refresh-Token");


        ResponseCookie refreshCookie = ResponseCookie.from("Refresh-Token", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .sameSite("Strict")
            .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());

        ResponseCookie accessCookie = ResponseCookie.from("Access-Token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(15 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", accessCookie.toString());

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}