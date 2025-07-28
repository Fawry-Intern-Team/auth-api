package com.example.auth_service;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.service.JwtService;
import com.example.auth_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//
//    @Test
//    void testRegister() {
//        UserDTO user = new UserDTO();
//        user.setEmail("test@example.com");
//        user.setPassword("password123");
//        user.setRoles(Arrays.asList("USER"));
//        when(jwtService.generateAccessToken(anyString(), anyList())).thenReturn("access-token");
//        when(jwtService.generateRefreshToken(anyString(), anyList())).thenReturn("refresh-token");
//        Map<String, String> tokens = userService.register(user);
//        assertEquals("access-token", tokens.get("Access-Token"));
//        assertEquals("refresh-token", tokens.get("Refresh-Token"));
//    }

//    @Test
//    void testLoginSuccess() {
//        UserDTO user = new UserDTO();
//        user.setEmail("test@example.com");
//        user.setPassword("password123");
//        user.setRoles(Arrays.asList("USER"));
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(jwtService.generateAccessToken(anyString(), anyList())).thenReturn("access-token");
//        when(jwtService.generateRefreshToken(anyString(), anyList())).thenReturn("refresh-token");
//        Map<String, String> tokens = userService.login(user);
//        assertEquals("access-token", tokens.get("Access-Token"));
//        assertEquals("refresh-token", tokens.get("Refresh-Token"));
//    }

//    @Test
//    void testLoginFailure() {
//        UserDTO user = new UserDTO();
//        user.setEmail("test@example.com");
//        user.setPassword("wrongpassword");
//        user.setRoles(Arrays.asList("USER"));
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
//        when(authentication.isAuthenticated()).thenReturn(false);
//        assertThrows(BadCredentialsException.class, () -> userService.login(user));
//    }
} 