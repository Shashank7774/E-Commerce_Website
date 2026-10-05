package com.ecommerce.backend.admin.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateAdminProductRequest(

        @NotBlank(message = "Product name is required")
        @Size(
                max = 200,
                message = "Product name must not exceed 200 characters"
        )
        String name,

        @NotBlank(message = "Product slug is required")
        @Size(
                max = 150,
                message = "Product slug must not exceed 150 characters"
        )
        String slug,

        @NotBlank(message = "Brand is required")
        @Size(
                max = 150,
                message = "Brand must not exceed 150 characters"
        )
        String brand,

        @NotBlank(message = "Category is required")
        @Size(
                max = 50,
                message = "Category must not exceed 50 characters"
        )
        String category,

        @NotNull(message = "Price is required")
        @DecimalMin(
                value = "0.00",
                message = "Price must be zero or greater"
        )
        BigDecimal price,

        @NotNull(message = "Original price is required")
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

        @NotNull(message = "Stock is required")
        @PositiveOrZero(
                message = "Stock must be zero or greater"
        )
        Integer stock
) {
}