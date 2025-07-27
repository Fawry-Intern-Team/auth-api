package com.example.auth_service.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;

@Configuration
public class JwtDecoderConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        return NimbusJwtDecoder.withSecretKey(key).build();
    }
}
