package com.ecommerce.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(
                message = "Email is required"
        )
        @Email(
                message = "Invalid email address"
        )
        @Size(
                max = 255,
                message = "Email is too long"
        )
        String email,

        @NotBlank(
                message = "Password is required"
        )
        @Size(
                min = 8,
                max = 100,
                message = "Invalid password"
        )
        String password
) {
}