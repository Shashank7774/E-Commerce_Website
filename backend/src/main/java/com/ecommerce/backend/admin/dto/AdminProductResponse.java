package com.ecommerce.backend.admin.dto;

import java.math.BigDecimal;

public record AdminProductResponse(

        Long id,

        String slug,

        String name,

        String brand,

        String category,

        BigDecimal price,

        BigDecimal originalPrice,

        BigDecimal rating,

        Integer reviews,

        String imageUrl,

        String badge,

        Integer stock,

        Long version
) {
}