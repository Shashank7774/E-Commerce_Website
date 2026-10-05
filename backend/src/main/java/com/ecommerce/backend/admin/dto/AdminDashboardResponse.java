package com.ecommerce.backend.admin.dto;

import java.math.BigDecimal;

public record AdminDashboardResponse(

        // ==========================================
        // Order Metrics
        // ==========================================

        long totalOrders,

        long placedOrders,

        long confirmedOrders,

        long shippedOrders,

        long deliveredOrders,

        long cancelledOrders,

        // ==========================================
        // Financial Metrics
        // ==========================================

        BigDecimal totalRevenue,

        // ==========================================
        // Customer Metrics
        // ==========================================

        long totalCustomers

) {}