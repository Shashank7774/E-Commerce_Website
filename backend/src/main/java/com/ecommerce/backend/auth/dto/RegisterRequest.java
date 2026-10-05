package com.ecommerce.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(
                message = "First name is required"
        )
        @Size(
                max = 100,
                message = "First name is too long"
        )
        String firstName,

        @NotBlank(
                message = "Last name is required"
        )
        @Size(
                max = 100,
                message = "Last name is too long"
        )
        String lastName,

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
                message = "Phone number is required"
        )
        @Pattern(
                regexp = "^[6-9][0-9]{9}$",
                message = "Invalid Indian mobile number"
        )
        String phone,

        @NotBlank(
                message = "Password is required"
        )
        @Size(
                min = 8,
                max = 100,
                message = "Password must be between 8 and 100 characters"
        )
        String password
) {
}