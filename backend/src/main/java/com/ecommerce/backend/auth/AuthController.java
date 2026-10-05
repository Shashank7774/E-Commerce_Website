package com.ecommerce.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.auth.dto.LoginRequest;
import com.ecommerce.backend.auth.dto.LoginResponse;
import com.ecommerce.backend.auth.dto.RegisterRequest;
import com.ecommerce.backend.auth.dto.RegisterResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager
            authenticationManager;

    private final SecurityContextRepository
            securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager
    ) {
        this.authService =
                authService;

        this.authenticationManager =
                authenticationManager;
    }

    // ==========================================
    // CSRF Token
    // ==========================================

    @GetMapping("/csrf")
    public ResponseEntity<Void> csrf(
            CsrfToken csrfToken
    ) {

        // Accessing the token forces Spring Security
        // to generate and expose the CSRF cookie.
        csrfToken.getToken();

        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // Customer Registration
    // ==========================================

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid
            @RequestBody
            RegisterRequest request
    ) {

        RegisterResponse response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==========================================
    // Customer Login
    // ==========================================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody
            LoginRequest request,

            HttpServletRequest httpRequest,

            HttpServletResponse httpResponse
    ) {

        String email =
                request.email()
                        .trim()
                        .toLowerCase();

        // --------------------------------------
        // Authenticate credentials
        // --------------------------------------

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                request.password()
                        )
                );

        // --------------------------------------
        // Create Security Context
        // --------------------------------------

        SecurityContext context =
                SecurityContextHolder
                        .createEmptyContext();

        context.setAuthentication(
                authentication
        );

        SecurityContextHolder.setContext(
                context
        );

        // --------------------------------------
        // Persist Security Context
        // into HTTP Session
        // --------------------------------------

        securityContextRepository.saveContext(
                context,
                httpRequest,
                httpResponse
        );

        // --------------------------------------
        // Build safe response
        // --------------------------------------

        LoginResponse response =
                authService.getLoginResponse(
                        email
                );

        return ResponseEntity.ok(
                response
        );
    }

    // ==========================================
    // Current Authenticated User
    // ==========================================

    @GetMapping("/me")
    public ResponseEntity<LoginResponse> me(
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        LoginResponse response =
                authService.getLoginResponse(
                        email
                );

        return ResponseEntity.ok(
                response
        );
    }

    // ==========================================
    // Logout
    // ==========================================

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request
    ) {

        // --------------------------------------
        // Clear SecurityContext
        // --------------------------------------

        SecurityContextHolder.clearContext();

        // --------------------------------------
        // Invalidate HTTP session
        // --------------------------------------

        var session =
                request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity
                .noContent()
                .build();
    }
}