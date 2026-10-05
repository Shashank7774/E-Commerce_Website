package com.ecommerce.backend.product;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.product.dto.ProductResponse;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService
    ) {
        this.productService = productService;
    }

    // ==========================================
    // Get products with:
    // Search
    // Category
    // Price filters
    // Sorting
    // Pagination
    // ==========================================

    @GetMapping
    public Page<ProductResponse> searchProducts(
            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            String category,

            @RequestParam(required = false)
            BigDecimal minPrice,

            @RequestParam(required = false)
            BigDecimal maxPrice,

            Pageable pageable
    ) {

        return productService.searchProducts(
                search,
                category,
                minPrice,
                maxPrice,
                pageable
        );
    }

    // ==========================================
    // Get products with pagination
    // ==========================================

    @GetMapping("/page")
    public Page<ProductResponse> getProducts(
            Pageable pageable
    ) {

        return productService.getProducts(
                pageable
        );
    }

    // ==========================================
    // Get deal products
    //
    // Default minimum discount = 20%
    // ==========================================

    @GetMapping("/deals")
    public Page<ProductResponse> getDealProducts(
            @RequestParam(
                    defaultValue = "20"
            )
            BigDecimal minimumDiscount,

            Pageable pageable
    ) {

        return productService.getDealProducts(
                minimumDiscount,
                pageable
        );
    }

    // ==========================================
    // Get products by category
    // ==========================================

    @GetMapping("/category/{category}")
    public List<ProductResponse> getProductsByCategory(
            @PathVariable String category
    ) {

        return productService.getProductsByCategory(
                category
        );
    }

    // ==========================================
    // Get single product by slug
    // ==========================================

    @GetMapping("/{slug}")
    public ProductResponse getProductBySlug(
            @PathVariable String slug
    ) {

        return productService.getProductBySlug(
                slug
        );
    }
}