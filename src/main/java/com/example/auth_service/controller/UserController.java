package com.example.auth_service.controller;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.dto.UserLoginDTO;
import com.example.auth_service.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO request,
                                      HttpServletResponse response) {
        return userService.register(request, response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO request,
                                   HttpServletResponse response) {
        return userService.login(request, response);
    }

    @GetMapping("/clear")
    public ResponseEntity<Void> clearCookies(HttpServletResponse response) {
        userService.deleteCookie("Access-Token", response);
        userService.deleteCookie("Refresh-Token", response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/clear-access")
    public ResponseEntity<Void> clearAccessCookies(HttpServletResponse response) {
        userService.deleteCookie("Access-Token", response);
        return ResponseEntity.ok().build();
    }
}