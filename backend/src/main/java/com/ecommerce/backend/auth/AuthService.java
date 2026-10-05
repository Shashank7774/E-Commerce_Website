package com.ecommerce.backend.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.auth.dto.LoginRequest;
import com.ecommerce.backend.auth.dto.LoginResponse;
import com.ecommerce.backend.auth.dto.RegisterRequest;
import com.ecommerce.backend.auth.dto.RegisterResponse;
import com.ecommerce.backend.user.User;
import com.ecommerce.backend.user.UserRepository;
import com.ecommerce.backend.user.UserRole;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager
            authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository =
                userRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.authenticationManager =
                authenticationManager;
    }

    // ==========================================
    // Register Customer
    // ==========================================

    @Transactional
    public RegisterResponse register(
            RegisterRequest request
    ) {

        String email =
                request.email()
                        .trim()
                        .toLowerCase();

        // --------------------------------------
        // Check existing account
        // --------------------------------------

        if (
                userRepository.existsByEmail(
                        email
                )
        ) {
            throw new IllegalArgumentException(
                    "An account with this email already exists."
            );
        }

        // --------------------------------------
        // Create User
        // --------------------------------------

        User user = new User();

        user.setFirstName(
                request.firstName().trim()
        );

        user.setLastName(
                request.lastName().trim()
        );

        user.setEmail(email);

        user.setPhone(
                request.phone().trim()
        );

        // --------------------------------------
        // Never store raw password
        // --------------------------------------

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.password()
                )
        );

        // --------------------------------------
        // Public registration always creates
        // a CUSTOMER account.
        // --------------------------------------

        user.setRole(
                UserRole.CUSTOMER
        );

        user.setEnabled(true);

        User savedUser =
                userRepository.save(user);

        // --------------------------------------
        // Return safe response
        // --------------------------------------

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }

    // ==========================================
    // Login
    // ==========================================

    @Transactional(readOnly = true)
    public LoginResponse login(
            LoginRequest request
    ) {

        String email =
                request.email()
                        .trim()
                        .toLowerCase();

        try {

            // ----------------------------------
            // Authenticate through Spring
            // Security
            // ----------------------------------

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    request.password()
                            )
                    );

            // ----------------------------------
            // Verify authenticated principal
            // ----------------------------------

            Object principal =
                    authentication.getPrincipal();

            if (
                    !(principal
                            instanceof UserDetails)
            ) {
                throw new BadCredentialsException(
                        "Invalid authentication."
                );
            }

            // ----------------------------------
            // Load application user
            // ----------------------------------

            User user =
                    userRepository
                            .findByEmail(email)
                            .orElseThrow(
                                    () ->
                                            new BadCredentialsException(
                                                    "Invalid authentication."
                                            )
                            );

            // ----------------------------------
            // Account status
            // ----------------------------------

            if (!user.isEnabled()) {

                throw new IllegalArgumentException(
                        "Your account is disabled."
                );
            }

            // ----------------------------------
            // Return safe response
            // ----------------------------------

            return new LoginResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole()
            );

        } catch (
                AuthenticationException exception
        ) {

            // ----------------------------------
            // Do not reveal whether the email
            // exists or the password is wrong.
            // ----------------------------------

            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }
    }

    // ==========================================
    // Build Safe Login Response
    // ==========================================

    @Transactional(readOnly = true)
    public LoginResponse getLoginResponse(
            String email
    ) {

        String normalizedEmail =
                email.trim().toLowerCase();

        User user =
                userRepository
                        .findByEmail(
                                normalizedEmail
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Invalid email or password."
                                        )
                        );

        // --------------------------------------
        // Account status
        // --------------------------------------

        if (!user.isEnabled()) {

            throw new IllegalArgumentException(
                    "Your account is disabled."
            );
        }

        // --------------------------------------
        // Return safe response
        // --------------------------------------

        return new LoginResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}