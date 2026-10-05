package com.ecommerce.backend.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
                JpaSpecificationExecutor<Product> {

    // ==========================================
    // Find Product
    // ==========================================

    Optional<Product> findBySlug(String slug);

    // ==========================================
    // Find Products By Category
    // ==========================================

    List<Product> findByCategoryIgnoreCase(
            String category
    );

    // ==========================================
    // Atomic Stock Decrement
    // ==========================================
    //
    // Decreases stock only when enough stock
    // is available.
    //
    // Example:
    //
    // stock = 38
    // quantity = 2
    //
    // result:
    // stock = 36
    //
    // If stock < quantity:
    // no row is updated.
    //
    // ==========================================

    @Modifying(flushAutomatically = true)
    @Query("""
            UPDATE Product p
            SET p.stock = p.stock - :quantity
            WHERE p.id = :productId
              AND p.stock >= :quantity
            """)
    int decrementStockIfAvailable(
            @Param("productId") Long productId,
            @Param("quantity") int quantity
    );

    // ==========================================
    // Atomic Stock Restore
    // ==========================================
    //
    // Used when an eligible order is cancelled.
    //
    // Example:
    //
    // stock = 38
    // cancelled quantity = 2
    //
    // result:
    // stock = 40
    //
    // The database performs the increment
    // atomically.
    //
    // ==========================================

    @Modifying(flushAutomatically = true)
    @Query("""
            UPDATE Product p
            SET p.stock = p.stock + :quantity
            WHERE p.id = :productId
            """)
    int restoreStock(
            @Param("productId") Long productId,
            @Param("quantity") int quantity
    );

    // ==========================================
    // Read Current Stock
    // ==========================================

    @Query("""
            SELECT p.stock
            FROM Product p
            WHERE p.id = :productId
            """)
    Integer findStockById(
            @Param("productId") Long productId
    );
}