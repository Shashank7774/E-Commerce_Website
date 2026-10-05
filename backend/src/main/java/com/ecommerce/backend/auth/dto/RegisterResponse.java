package com.ecommerce.backend.auth.dto;

import java.time.OffsetDateTime;

import com.ecommerce.backend.user.UserRole;

public record RegisterResponse(

        Long id,

        String firstName,

        String lastName,

        String email,

        String phone,

        UserRole role,

        OffsetDateTime createdAt
) {
}