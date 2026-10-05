package com.ecommerce.backend.upload;

public record ProductImageUploadResponse(
        String imageUrl,
        String fileName
) {
}