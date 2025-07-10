package com.example.auth_service.filter;


import com.example.auth_service.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }



    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Triggered filter");
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        final String email = jwtService.extractClaim(token, Claims::getSubject);
        System.out.println(jwtService.validateToken(token));
        if(!jwtService.validateToken(token)) {
            final String refreshTokenHeader = request.getHeader("Refresh-Token");
            if(refreshTokenHeader != null && jwtService.validateToken(refreshTokenHeader)) {
                token = jwtService.generateRefreshToken(email);
            } else {
                throw new BadCredentialsException("Invalid token");
            }
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            List<String> roles = jwtService.extractClaim(token,
                    claims -> claims.get("roles", List.class));

            List<SimpleGrantedAuthority> authorities = null;
            try {
                authorities = roles
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
