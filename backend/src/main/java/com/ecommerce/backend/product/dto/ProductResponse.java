package com.ecommerce.backend.product.dto;

import java.math.BigDecimal;

public record ProductResponse(
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
        Integer stock
) {
}