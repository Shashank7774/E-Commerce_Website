package com.ecommerce.backend.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository
        extends JpaRepository<User, Long> {

    // ==========================================
    // Find User By Email
    // ==========================================

    Optional<User> findByEmail(
            String email
    );

    // ==========================================
    // Check Email Exists
    // ==========================================

    boolean existsByEmail(
            String email
    );

    // ==========================================
    // Count Customers
    // ==========================================
    //
    // Admin accounts are excluded.
    // Only users having CUSTOMER role are counted.
    //
    // ==========================================

    @Query("""
            SELECT COUNT(u)
            FROM User u
            WHERE u.role =
                  com.ecommerce.backend.user.UserRole.CUSTOMER
            """)
    long countCustomers();
}