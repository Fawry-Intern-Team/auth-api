package com.example.auth_service.service;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    @Autowired
    private JwtService jwtService;

    public String refresh(String refreshToken) {
        if(jwtService.validateToken(refreshToken)) {
            return jwtService.generateAccessToken(jwtService.extractClaim(refreshToken, Claims::getSubject), null);
        } else throw new BadCredentialsException("Invalid refresh token");
    }
}
