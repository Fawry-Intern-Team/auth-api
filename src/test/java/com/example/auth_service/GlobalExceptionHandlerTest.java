package com.example.auth_service;

import com.example.auth_service.exception.GlobalExceptionHandler;
import com.example.auth_service.exception.InvalidTokenException;
import com.example.auth_service.exception.TokenExpiredException;
import com.example.auth_service.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleValidationErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("userDTO", "email", "Invalid email format");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationErrors(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().get("error"));
        assertEquals("400", response.getBody().get("status"));
    }

    @Test
    void testHandleBadCredentials() {
        BadCredentialsException ex = new BadCredentialsException("Invalid credentials");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleBadCredentials(ex);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Authentication failed", response.getBody().get("error"));
        assertEquals("Invalid credentials", response.getBody().get("message"));
        assertEquals("401", response.getBody().get("status"));
    }

    @Test
    void testHandleUsernameNotFound() {
        UsernameNotFoundException ex = new UsernameNotFoundException("User not found");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleUsernameNotFound(ex);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().get("error"));
        assertEquals("User does not exist", response.getBody().get("message"));
        assertEquals("404", response.getBody().get("status"));
    }

    @Test
    void testHandleNotFound() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/api/test", null);
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleNotFound(ex);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Not found", response.getBody().get("error"));
        assertEquals("The requested endpoint does not exist", response.getBody().get("message"));
        assertEquals("404", response.getBody().get("status"));
    }

    @Test
    void testHandleUserAlreadyExists() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("User already exists");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleUserAlreadyExists(ex);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User already exists", response.getBody().get("error"));
        assertEquals("User already exists", response.getBody().get("message"));
        assertEquals("409", response.getBody().get("status"));
    }

    @Test
    void testHandleInvalidToken() {
        InvalidTokenException ex = new InvalidTokenException("Invalid token format");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleInvalidToken(ex);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid token", response.getBody().get("error"));
        assertEquals("Invalid token format", response.getBody().get("message"));
        assertEquals("401", response.getBody().get("status"));
    }

    @Test
    void testHandleTokenExpired() {
        TokenExpiredException ex = new TokenExpiredException("Token has expired");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleTokenExpired(ex);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Token expired", response.getBody().get("error"));
        assertEquals("Token has expired", response.getBody().get("message"));
        assertEquals("401", response.getBody().get("status"));
    }

    @Test
    void testHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleIllegalArgument(ex);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid request", response.getBody().get("error"));
        assertEquals("Invalid argument", response.getBody().get("message"));
        assertEquals("400", response.getBody().get("status"));
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Unexpected error");
        
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(ex);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().get("error"));
        assertEquals("An unexpected error occurred", response.getBody().get("message"));
        assertEquals("500", response.getBody().get("status"));
    }
} 