package com.ecommerce.backend.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.admin.dto.AdminProductResponse;
import com.ecommerce.backend.admin.dto.CreateAdminProductRequest;
import com.ecommerce.backend.admin.dto.UpdateAdminProductRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(
            AdminProductService adminProductService
    ) {
        this.adminProductService =
                adminProductService;
    }

    // =========================================================
    // GET ALL PRODUCTS
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<AdminProductResponse>> getAllProducts(
            Pageable pageable
    ) {

        Page<AdminProductResponse> products =
                adminProductService.getAllProducts(
                        pageable
                );

        return ResponseEntity.ok(products);
    }

    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    @GetMapping("/{productId}")
    public ResponseEntity<AdminProductResponse> getProductById(
            @PathVariable Long productId
    ) {

        AdminProductResponse product =
                adminProductService.getProductById(
                        productId
                );

        return ResponseEntity.ok(product);
    }

    // =========================================================
    // CREATE PRODUCT
    // =========================================================

    @PostMapping
    public ResponseEntity<AdminProductResponse> createProduct(
            @Valid
            @RequestBody
            CreateAdminProductRequest request
    ) {

        AdminProductResponse product =
                adminProductService.createProduct(
                        request
                );

        return ResponseEntity.ok(product);
    }

    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    @PatchMapping("/{productId}")
    public ResponseEntity<AdminProductResponse> updateProduct(
            @PathVariable Long productId,

            @Valid
            @RequestBody
            UpdateAdminProductRequest request
    ) {

        AdminProductResponse product =
                adminProductService.updateProduct(
                        productId,
                        request
                );

        return ResponseEntity.ok(product);
    }

    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId
    ) {

        adminProductService.deleteProduct(
                productId
        );

        return ResponseEntity.noContent().build();
    }
}