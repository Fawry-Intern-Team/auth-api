package com.example.auth_service;

import com.example.auth_service.controller.UserController;
import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        UserDTO user = new UserDTO();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRoles(Arrays.asList("USER"));
        Map<String, String> tokens = new HashMap<>();
        tokens.put("Access-Token", "access-token");
        tokens.put("Refresh-Token", "refresh-token");
        when(userService.register(any(UserDTO.class))).thenReturn(tokens);
        ResponseEntity<?> response = userController.register(user);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(tokens, response.getBody());
    }

    @Test
    void testLogin() {
        UserDTO user = new UserDTO();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRoles(Arrays.asList("USER"));
        Map<String, String> tokens = new HashMap<>();
        tokens.put("Access-Token", "access-token");
        tokens.put("Refresh-Token", "refresh-token");
        when(userService.login(any(UserDTO.class))).thenReturn(tokens);
        ResponseEntity<?> response = userController.login(user);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tokens, response.getBody());
    }
} 