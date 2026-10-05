package com.ecommerce.backend.auth.dto;

import com.ecommerce.backend.user.UserRole;

public record LoginResponse(

        Long id,

        String firstName,

        String lastName,

        String email,

        String phone,

        UserRole role
) {
}