package com.ecommerce.backend.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.user.User;
import com.ecommerce.backend.user.UserRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository
    ) {
        this.userRepository =
                userRepository;
    }

    // ==========================================
    // Load User by Email
    // ==========================================

    @Override
    public UserDetails loadUserByUsername(
            String email
    ) throws UsernameNotFoundException {

        String normalizedEmail =
                email.trim().toLowerCase();

        User user =
                userRepository
                        .findByEmail(
                                normalizedEmail
                        )
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                "User not found."
                                        )
                        );

        // ======================================
        // Spring Security UserDetails
        // ======================================

        return org.springframework.security
                .core.userdetails.User
                .withUsername(
                        user.getEmail()
                )
                .password(
                        user.getPasswordHash()
                )
                .authorities(
                        new SimpleGrantedAuthority(
                                "ROLE_" +
                                        user.getRole()
                                                .name()
                        )
                )
                .disabled(
                        !user.isEnabled()
                )
                .build();
    }
}