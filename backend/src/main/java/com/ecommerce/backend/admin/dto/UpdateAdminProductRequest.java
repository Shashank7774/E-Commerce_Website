package com.ecommerce.backend.admin.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record UpdateAdminProductRequest(

        @Size(
                max = 200,
                message = "Product name must not exceed 200 characters"
        )
        String name,

        @Size(
                max = 150,
                message = "Product slug must not exceed 150 characters"
        )
        String slug,

        @Size(
                max = 150,
                message = "Brand must not exceed 150 characters"
        )
        String brand,

        @Size(
                max = 50,
                message = "Category must not exceed 50 characters"
        )
        String category,

        @DecimalMin(
                value = "0.00",
                message = "Price must be zero or greater"
        )
        BigDecimal price,

        @DecimalMin(
                value = "0.00",
                message = "Original price must be zero or greater"
        )
        BigDecimal originalPrice,

        @Size(
                max = 500,
                message = "Image URL must not exceed 500 characters"
        )
        String imageUrl,

        @Size(
                max = 100,
                message = "Badge must not exceed 100 characters"
        )
        String badge,

        Integer stock
) {
}