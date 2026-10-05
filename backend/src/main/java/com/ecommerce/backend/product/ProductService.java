package com.ecommerce.backend.product;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.product.dto.ProductResponse;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ==========================================
    // Get all products
    // ==========================================

    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::toProductResponse)
                .toList();
    }

    // ==========================================
    // Get products with pagination
    // ==========================================

    public Page<ProductResponse> getProducts(
            Pageable pageable
    ) {

        return productRepository
                .findAll(pageable)
                .map(this::toProductResponse);
    }

    // ==========================================
    // Get products with:
    // Search
    // Category
    // Minimum price
    // Maximum price
    // Sorting
    // Pagination
    // ==========================================

    public Page<ProductResponse> searchProducts(
            String search,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {

        Specification<Product> specification =
                Specification.allOf(
                        ProductSpecification.search(search),
                        ProductSpecification.hasCategory(category),
                        ProductSpecification.hasMinPrice(minPrice),
                        ProductSpecification.hasMaxPrice(maxPrice)
                );

        Pageable finalPageable =
                applyDiscountSortingIfRequested(pageable);

        return productRepository
                .findAll(
                        specification,
                        finalPageable
                )
                .map(this::toProductResponse);
    }

    // ==========================================
    // Get deal products
    //
    // Example:
    // minimumDiscount = 20
    //
    // Only products with >= 20% discount
    // will be returned.
    // ==========================================

    public Page<ProductResponse> getDealProducts(
            BigDecimal minimumDiscount,
            Pageable pageable
    ) {

        Specification<Product> specification =
                ProductSpecification.hasMinimumDiscount(
                        minimumDiscount
                );

        /*
         * Deals should always be displayed
         * from highest discount to lowest discount.
         *
         * Discount formula:
         *
         * (originalPrice - price) / originalPrice
         */

        Sort discountSort = JpaSort.unsafe(
                Sort.Direction.DESC,
                "(originalPrice - price) / originalPrice"
        );

        Pageable dealPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                discountSort
        );

        return productRepository
                .findAll(
                        specification,
                        dealPageable
                )
                .map(this::toProductResponse);
    }

    // ==========================================
    // Get products by category
    // ==========================================

    public List<ProductResponse> getProductsByCategory(
            String category
    ) {

        return productRepository
                .findByCategoryIgnoreCase(category)
                .stream()
                .map(this::toProductResponse)
                .toList();
    }

    // ==========================================
    // Get single product by slug
    // ==========================================

    public ProductResponse getProductBySlug(
            String slug
    ) {

        Product product = productRepository
                .findBySlug(slug)
                .orElseThrow(
                        () -> new ProductNotFoundException(
                                "Product not found: " + slug
                        )
                );

        return toProductResponse(product);
    }

    // ==========================================
    // Apply true discount sorting
    // ==========================================

    private Pageable applyDiscountSortingIfRequested(
            Pageable pageable
    ) {

        boolean discountSortRequested =
                pageable.getSort()
                        .stream()
                        .anyMatch(order ->
                                order.getProperty()
                                        .equalsIgnoreCase("discount")
                        );

        /*
         * If the requested sort is not discount,
         * keep the original Pageable untouched.
         */

        if (!discountSortRequested) {
            return pageable;
        }

        Sort.Direction direction =
                pageable.getSort()
                        .stream()
                        .filter(order ->
                                order.getProperty()
                                        .equalsIgnoreCase("discount")
                        )
                        .findFirst()
                        .map(Sort.Order::getDirection)
                        .orElse(
                                Sort.Direction.DESC
                        );

        /*
         * Actual discount expression:
         *
         * (originalPrice - price) / originalPrice
         */

        Sort discountSort = JpaSort.unsafe(
                direction,
                "(originalPrice - price) / originalPrice"
        );

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                discountSort
        );
    }

    // ==========================================
    // Entity → DTO
    // ==========================================

    private ProductResponse toProductResponse(
            Product product
    ) {

        return new ProductResponse(
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
                product.getStock()
        );
    }
}