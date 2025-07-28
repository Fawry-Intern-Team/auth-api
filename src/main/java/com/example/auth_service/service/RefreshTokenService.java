package com.example.auth_service.service;

import com.example.auth_service.enums.Role;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefreshTokenService {
    @Autowired
    private JwtService jwtService;

    public String refresh(String refreshToken) {
        if(refreshToken != null && jwtService.validateToken(refreshToken)) {
            String email = jwtService.extractClaim(refreshToken, Claims::getSubject);
            List<String> roles = jwtService.extractRoles(refreshToken);

            return jwtService.generateAccessToken(email, roles);
        } else throw new BadCredentialsException("Invalid refresh token");
    }
}
