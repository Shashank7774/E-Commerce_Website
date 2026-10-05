package com.ecommerce.backend.product;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecification {

    private ProductSpecification() {
    }

    // ==========================================
    // Category filter
    // ==========================================

    public static Specification<Product> hasCategory(
            String category
    ) {
        return (root, query, criteriaBuilder) -> {

            if (category == null || category.isBlank()) {
                return null;
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(
                            root.get("category")
                    ),
                    category.trim().toLowerCase()
            );
        };
    }

    // ==========================================
    // Minimum price filter
    // ==========================================

    public static Specification<Product> hasMinPrice(
            BigDecimal minPrice
    ) {
        return (root, query, criteriaBuilder) -> {

            if (minPrice == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("price"),
                    minPrice
            );
        };
    }

    // ==========================================
    // Maximum price filter
    // ==========================================

    public static Specification<Product> hasMaxPrice(
            BigDecimal maxPrice
    ) {
        return (root, query, criteriaBuilder) -> {

            if (maxPrice == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("price"),
                    maxPrice
            );
        };
    }

    // ==========================================
    // Search filter
    // ==========================================

    public static Specification<Product> search(
            String search
    ) {
        return (root, query, criteriaBuilder) -> {

            if (search == null || search.isBlank()) {
                return null;
            }

            String searchPattern =
                    "%" + search.trim().toLowerCase() + "%";

            return criteriaBuilder.or(

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("name")
                            ),
                            searchPattern
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("brand")
                            ),
                            searchPattern
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("category")
                            ),
                            searchPattern
                    )
            );
        };
    }

    // ==========================================
    // Minimum discount filter
    //
    // Discount percentage:
    //
    // ((originalPrice - price) / originalPrice) * 100
    //
    // Example:
    //
    // Original Price = 2000
    // Selling Price  = 1500
    //
    // Discount = 25%
    // ==========================================

    public static Specification<Product> hasMinimumDiscount(
            BigDecimal minimumDiscount
    ) {
        return (root, query, criteriaBuilder) -> {

            if (minimumDiscount == null) {
                return null;
            }

            /*
             * Convert the calculated expression explicitly
             * to BigDecimal so Criteria API can compare it
             * with minimumDiscount safely.
             */

            jakarta.persistence.criteria.Expression<BigDecimal>
                    discountPercentage =
                    criteriaBuilder.prod(
                            criteriaBuilder.quot(
                                    criteriaBuilder.diff(
                                            root.<BigDecimal>get(
                                                    "originalPrice"
                                            ),
                                            root.<BigDecimal>get(
                                                    "price"
                                            )
                                    ),
                                    root.<BigDecimal>get(
                                            "originalPrice"
                                    )
                            ),
                            criteriaBuilder.literal(
                                    BigDecimal.valueOf(100)
                            )
                    ).as(BigDecimal.class);

            return criteriaBuilder.greaterThanOrEqualTo(
                    discountPercentage,
                    minimumDiscount
            );
        };
    }
}