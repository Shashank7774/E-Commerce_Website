package com.ecommerce.backend.order;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    // ==========================================
    // Find Order By Order Number
    // ==========================================

    Optional<Order> findByOrderNumber(
            String orderNumber
    );

    // ==========================================
    // Find Order For Specific User
    // ==========================================

    Optional<Order> findByOrderNumberAndUserId(
            String orderNumber,
            Long userId
    );

    // ==========================================
    // Check Order Number
    // ==========================================

    boolean existsByOrderNumber(
            String orderNumber
    );

    // ==========================================
    // Customer Orders
    // ==========================================

    Page<Order> findByUserIdOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable
    );

    // ==========================================
    // Total Revenue
    // ==========================================
    //
    // Cancelled orders are excluded from revenue.
    //
    // COALESCE prevents null when there are
    // no matching orders.
    //
    // ==========================================

    @Query("""
            SELECT COALESCE(SUM(o.total), 0)
            FROM Order o
            WHERE o.status <> com.ecommerce.backend.order.OrderStatus.CANCELLED
            """)
    BigDecimal calculateTotalRevenue();

    // ==========================================
    // Count By Status
    // ==========================================

    long countByStatus(
            OrderStatus status
    );
}