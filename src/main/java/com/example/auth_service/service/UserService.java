package com.example.auth_service.service;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.dto.UserLoginDTO;
import com.example.auth_service.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> register(UserDTO request, HttpServletResponse response) {
        logger.info("Registering user with email: {}", request.getEmail());
        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        ResponseEntity<?> restResponse = null;
        try {
            restResponse = restTemplate.postForEntity(
                    "http://user-service/api/users",
                    request,
                    String.class
            );
        } catch (RestClientException e) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

//        if (restResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
//            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
//        }

        String accessToken = jwtService.generateAccessToken(request.getEmail(), request.getRoles());
        String refreshToken = jwtService.generateRefreshToken(request.getEmail(), request.getRoles());

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

        response.addHeader("Set-Cookie", refreshCookie.toString());
        response.addHeader("Set-Cookie", accessCookie.toString());

        return ResponseEntity
                .status(restResponse.getStatusCode())
                .body(restResponse.getBody());
    }



    public ResponseEntity<?> login(@Valid UserLoginDTO request,
                                   HttpServletResponse response) {
        logger.info("Attempting login for email: {}", request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if (!authentication.isAuthenticated()) {
            logger.warn("Login failed for email: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .collect(Collectors.toList());

        String accessToken = jwtService.generateAccessToken(request.getEmail(), roles);
        String refreshToken = jwtService.generateRefreshToken(request.getEmail(), roles);

        int refreshMaxAge = !request.isKeepLoggedIn() ? -1 : 7 * 24 * 60 * 60;
        int accessMaxAge = !request.isKeepLoggedIn() ? -1 : 15 * 60;

        ResponseCookie refreshCookie = ResponseCookie.from("Refresh-Token", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshMaxAge)
                .sameSite("Strict")
                .build();

        ResponseCookie accessCookie = ResponseCookie.from("Access-Token", accessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(accessMaxAge)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());
        response.addHeader("Set-Cookie", accessCookie.toString());

        logger.info("Login successful for email: {}", request.getEmail());
        return ResponseEntity.accepted().build();
    }

    public void deleteCookie(String name, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
