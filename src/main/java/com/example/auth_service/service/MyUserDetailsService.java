package com.example.auth_service.service;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.model.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings("unused")
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                    "http://user-service/api/users/by-email?email=" + email,
                    UserDTO.class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return new UserPrinciple(response.getBody());
            } else {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
        } catch (RestClientException e) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}
