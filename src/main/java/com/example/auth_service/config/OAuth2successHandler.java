package com.example.auth_service.config;

import com.example.auth_service.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuth2successHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email");

        String accessToken = jwtService.generateAccessToken(email, null);
        String refreshToken = jwtService.generateRefreshToken(email, null);

        // Set refresh token as HttpOnly cookie
        ResponseCookie refreshCookie = ResponseCookie.from("Refresh-Token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        String redirectUrl = "http://localhost:3000/login-success#accessToken=" + accessToken;
        response.sendRedirect(redirectUrl);
    }

}
