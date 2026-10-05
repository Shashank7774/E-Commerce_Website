package com.ecommerce.backend.admin;

import java.math.BigDecimal;

import com.ecommerce.backend.admin.dto.AdminDashboardResponse;
import com.ecommerce.backend.order.OrderRepository;
import com.ecommerce.backend.order.OrderStatus;
import com.ecommerce.backend.user.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminDashboardService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public AdminDashboardService(
            OrderRepository orderRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // ==========================================
    // Get Dashboard Statistics
    // ==========================================

    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboardStats() {

        // --------------------------------------
        // Total Orders
        // --------------------------------------

        long totalOrders =
                orderRepository.count();

        // --------------------------------------
        // Orders By Status
        // --------------------------------------

        long placedOrders =
                orderRepository.countByStatus(
                        OrderStatus.PLACED
                );

        long confirmedOrders =
                orderRepository.countByStatus(
                        OrderStatus.CONFIRMED
                );

        long shippedOrders =
                orderRepository.countByStatus(
                        OrderStatus.SHIPPED
                );

        long deliveredOrders =
                orderRepository.countByStatus(
                        OrderStatus.DELIVERED
                );

        long cancelledOrders =
                orderRepository.countByStatus(
                        OrderStatus.CANCELLED
                );

        // --------------------------------------
        // Total Revenue
        // --------------------------------------

        BigDecimal totalRevenue =
                orderRepository.calculateTotalRevenue();

        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        // --------------------------------------
        // Total Customers
        // --------------------------------------

        long totalCustomers =
                userRepository.countCustomers();

        // --------------------------------------
        // Build Response
        // --------------------------------------

        return new AdminDashboardResponse(
                totalOrders,

                placedOrders,

                confirmedOrders,

                shippedOrders,

                deliveredOrders,

                cancelledOrders,

                totalRevenue,

                totalCustomers
        );
    }
}