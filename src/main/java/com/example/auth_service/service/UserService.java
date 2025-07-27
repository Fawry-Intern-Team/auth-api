package com.example.auth_service.service;

import com.example.auth_service.dto.UserDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public Map<String, String> register(UserDTO request) {
        logger.info("Registering user with email: {}", request.getEmail());
        
        // TODO: Check if user already exists
        // if (userRepository.existsByEmail(request.getEmail())) {
        //     throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        // }
        
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        // TODO: Save the user by calling the user service
        // userRepository.save(request);
        
        HashMap<String, String> tokens = new HashMap<>();
        tokens.put("Access-Token",
                jwtService.generateAccessToken(request.getEmail(), request.getRoles()));
        tokens.put("Refresh-Token",
                jwtService.generateRefreshToken(request.getEmail(), request.getRoles()));
        logger.info("User registered successfully: {}", request.getEmail());
        return tokens;
    }

    public Map<String, String> login(@Valid UserDTO request) {
        logger.info("Attempting login for email: {}", request.getEmail());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),request.getPassword()));
        HashMap<String, String> tokens = new HashMap<>();

        if(authentication.isAuthenticated()) {
            tokens.put("Access-Token",
                    jwtService.generateAccessToken(request.getEmail(), request.getRoles()));
            tokens.put("Refresh-Token",
                    jwtService.generateRefreshToken(request.getEmail(), request.getRoles()));
            logger.info("Login successful for email: {}", request.getEmail());
            return tokens;
        } else {
            logger.warn("Login failed for email: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
