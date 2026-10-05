package com.ecommerce.backend.admin;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.admin.dto.AdminProductResponse;
import com.ecommerce.backend.admin.dto.CreateAdminProductRequest;
import com.ecommerce.backend.admin.dto.UpdateAdminProductRequest;
import com.ecommerce.backend.product.Product;
import com.ecommerce.backend.product.ProductRepository;
import com.ecommerce.backend.upload.ProductImageStorageService;

@Service
public class AdminProductService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    AdminProductService.class
            );

    private final ProductRepository productRepository;

    private final ProductImageStorageService
            productImageStorageService;

    public AdminProductService(
            ProductRepository productRepository,
            ProductImageStorageService productImageStorageService
    ) {
        this.productRepository =
                productRepository;

        this.productImageStorageService =
                productImageStorageService;
    }

    // =========================================================
    // GET ALL PRODUCTS
    // =========================================================

    @Transactional(readOnly = true)
    public Page<AdminProductResponse> getAllProducts(
            Pageable pageable
    ) {

        return productRepository
                .findAll(pageable)
                .map(this::toResponse);
    }

    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public AdminProductResponse getProductById(
            Long productId
    ) {

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Product not found."
                                )
                        );

        return toResponse(product);
    }

    // =========================================================
    // CREATE PRODUCT
    // =========================================================

    @Transactional
    public AdminProductResponse createProduct(
            CreateAdminProductRequest request
    ) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Product data is required."
            );
        }

        String slug =
                request.slug()
                        .trim();

        if (
                productRepository
                        .findBySlug(slug)
                        .isPresent()
        ) {

            throw new IllegalArgumentException(
                    "A product with this slug already exists."
            );
        }

        if (
                request.imageUrl() == null ||
                request.imageUrl().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Product image URL is required."
            );
        }

        if (
                request.originalPrice()
                        .compareTo(
                                request.price()
                        ) < 0
        ) {

            throw new IllegalArgumentException(
                    "Original price cannot be lower than selling price."
            );
        }

        Product product =
                new Product();

        product.setName(
                request.name().trim()
        );

        product.setSlug(
                slug
        );

        product.setBrand(
                request.brand().trim()
        );

        product.setCategory(
                request.category().trim()
        );

        product.setPrice(
                request.price()
        );

        product.setOriginalPrice(
                request.originalPrice()
        );

        product.setImageUrl(
                request.imageUrl().trim()
        );

        product.setBadge(
                normalizeOptionalText(
                        request.badge()
                )
        );

        product.setStock(
                request.stock()
        );

        // New products start with zero reviews/rating.
        product.setRating(
                BigDecimal.ZERO
        );

        product.setReviews(
                0
        );

        Product savedProduct =
                productRepository.save(
                        product
                );

        return toResponse(
                savedProduct
        );
    }

    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    @Transactional
    public AdminProductResponse updateProduct(
            Long productId,
            UpdateAdminProductRequest request
    ) {

        if (productId == null) {

            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        if (request == null) {

            throw new IllegalArgumentException(
                    "Product data is required."
            );
        }

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Product not found."
                                )
                        );

        // -----------------------------------------------------
        // Store old image URL.
        // We only delete it AFTER the database update succeeds.
        // -----------------------------------------------------

        String oldImageUrl =
                product.getImageUrl();

        // -----------------------------------------------------
        // NAME
        // -----------------------------------------------------

        if (
                request.name() != null &&
                !request.name().isBlank()
        ) {

            product.setName(
                    request.name().trim()
            );
        }

        // -----------------------------------------------------
        // SLUG
        // -----------------------------------------------------

        if (
                request.slug() != null &&
                !request.slug().isBlank()
        ) {

            String newSlug =
                    request.slug().trim();

            if (
                    !newSlug.equals(
                            product.getSlug()
                    )
            ) {

                productRepository
                        .findBySlug(newSlug)
                        .ifPresent(existing -> {

                            if (
                                    !existing.getId()
                                            .equals(productId)
                            ) {

                                throw new IllegalArgumentException(
                                        "A product with this slug already exists."
                                );
                            }
                        });

                product.setSlug(
                        newSlug
                );
            }
        }

        // -----------------------------------------------------
        // BRAND
        // -----------------------------------------------------

        if (
                request.brand() != null &&
                !request.brand().isBlank()
        ) {

            product.setBrand(
                    request.brand().trim()
            );
        }

        // -----------------------------------------------------
        // CATEGORY
        // -----------------------------------------------------

        if (
                request.category() != null &&
                !request.category().isBlank()
        ) {

            product.setCategory(
                    request.category().trim()
            );
        }

        // -----------------------------------------------------
        // PRICE
        // -----------------------------------------------------

        if (
                request.price() != null
        ) {

            product.setPrice(
                    request.price()
            );
        }

        // -----------------------------------------------------
        // ORIGINAL PRICE
        // -----------------------------------------------------

        if (
                request.originalPrice() != null
        ) {

            product.setOriginalPrice(
                    request.originalPrice()
            );
        }

        // -----------------------------------------------------
        // PRICE VALIDATION
        // -----------------------------------------------------

        if (
                product.getOriginalPrice()
                        .compareTo(
                                product.getPrice()
                        ) < 0
        ) {

            throw new IllegalArgumentException(
                    "Original price cannot be lower than selling price."
            );
        }

        // -----------------------------------------------------
        // IMAGE
        // -----------------------------------------------------

        if (
                request.imageUrl() != null &&
                !request.imageUrl().isBlank()
        ) {

            String newImageUrl =
                    request.imageUrl().trim();

            product.setImageUrl(
                    newImageUrl
            );
        }

        // -----------------------------------------------------
        // BADGE
        // -----------------------------------------------------

        if (
                request.badge() != null
        ) {

            product.setBadge(
                    normalizeOptionalText(
                            request.badge()
                    )
            );
        }

        // -----------------------------------------------------
        // STOCK
        // -----------------------------------------------------

        if (
                request.stock() != null
        ) {

            if (
                    request.stock() < 0
            ) {

                throw new IllegalArgumentException(
                        "Stock cannot be negative."
                );
            }

            product.setStock(
                    request.stock()
            );
        }

        // -----------------------------------------------------
        // SAVE DATABASE FIRST
        // -----------------------------------------------------

        Product savedProduct =
                productRepository.save(
                        product
                );

        // -----------------------------------------------------
        // CLEAN OLD IMAGE
        //
        // Only delete if:
        // 1. There was an old image.
        // 2. A new image was actually supplied.
        // 3. Old and new URLs are different.
        // -----------------------------------------------------

        String newImageUrl =
                savedProduct.getImageUrl();

        boolean imageChanged =
                oldImageUrl != null &&
                !oldImageUrl.isBlank() &&
                newImageUrl != null &&
                !newImageUrl.isBlank() &&
                !oldImageUrl.equals(
                        newImageUrl
                );

        if (imageChanged) {

            cleanupImageSafely(
                    oldImageUrl
            );
        }

        return toResponse(
                savedProduct
        );
    }

    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    @Transactional
    public void deleteProduct(
            Long productId
    ) {

        if (productId == null) {

            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Product not found."
                                )
                        );

        String imageUrl =
                product.getImageUrl();

        // -----------------------------------------------------
        // DELETE PRODUCT FROM DATABASE
        // -----------------------------------------------------

        productRepository.delete(
                product
        );

        // -----------------------------------------------------
        // CLEAN ASSOCIATED IMAGE
        // -----------------------------------------------------

        cleanupImageSafely(
                imageUrl
        );
    }

    // =========================================================
    // SAFE IMAGE CLEANUP
    // =========================================================

    private void cleanupImageSafely(
            String imageUrl
    ) {

        if (
                imageUrl == null ||
                imageUrl.isBlank()
        ) {
            return;
        }

        try {

            productImageStorageService
                    .deleteByImageUrl(
                            imageUrl
                    );

        } catch (RuntimeException exception) {

            /*
             * The database operation has already succeeded.
             *
             * We do not convert a successful product operation
             * into a failed request just because filesystem
             * cleanup failed.
             *
             * The failure is logged so that it can be cleaned
             * later by an operational/background cleanup job.
             */

            logger.error(
                    "Unable to clean product image: {}",
                    imageUrl,
                    exception
            );
        }
    }

    // =========================================================
    // RESPONSE MAPPER
    // =========================================================

    private AdminProductResponse toResponse(
            Product product
    ) {

        return new AdminProductResponse(
                product.getId(),
                product.getSlug(),
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                product.getPrice(),
                product.getOriginalPrice(),
                product.getRating(),
                product.getReviews(),
                product.getImageUrl(),
                product.getBadge(),
                product.getStock(),
                product.getVersion()
        );
    }

    // =========================================================
    // OPTIONAL TEXT NORMALIZATION
    // =========================================================

    private String normalizeOptionalText(
            String value
    ) {

        if (
                value == null ||
                value.isBlank()
        ) {
            return null;
        }

        return value.trim();
    }
}