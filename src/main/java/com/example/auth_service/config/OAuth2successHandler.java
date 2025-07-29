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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.List;

@RequiredArgsConstructor
@Component
public class OAuth2successHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String name = user.getAttribute("name");
        String picture = user.getAttribute("picture");
        String email = user.getAttribute("email");
        String googleUserId = user.getAttribute("sub");

// Default role
        List<String> roles = List.of("USER");

// Generate tokens
        String accessToken = jwtService.generateAccessToken(email, roles);
        String refreshToken = jwtService.generateRefreshToken(email, roles);

// Set secure cookies
        ResponseCookie refreshCookie = ResponseCookie.from("Refresh-Token", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        ResponseCookie accessCookie = ResponseCookie.from("Access-Token", accessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(15 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        // Convert roles to a comma-separated string for the URL
        String rolesParam = URLEncoder.encode(String.join(",", roles), StandardCharsets.UTF_8);

        // Redirect with non-sensitive user info
        String redirectUrl = String.format(
                "http://localhost:4200/login-success?email=%s&name=%s&picture=%s&googleId=%s&roles=%s",
                URLEncoder.encode(email, StandardCharsets.UTF_8),
                URLEncoder.encode(name, StandardCharsets.UTF_8),
                URLEncoder.encode(picture, StandardCharsets.UTF_8),
                URLEncoder.encode(googleUserId, StandardCharsets.UTF_8),
                rolesParam
        );

        response.sendRedirect(redirectUrl);
    }
}
