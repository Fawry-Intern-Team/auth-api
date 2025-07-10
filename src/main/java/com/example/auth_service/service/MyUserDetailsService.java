package com.example.auth_service.service;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.model.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.AbstractSingleValueEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: call user service to get the user
        UserDTO user = new UserDTO();
        user.setEmail(username);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        user.setPassword(bCryptPasswordEncoder.encode("password123"));

        if (user == null)
            throw new UsernameNotFoundException("NOT FOUND!!!!!!!!");
        return new UserPrinciple(user);
    }
}
