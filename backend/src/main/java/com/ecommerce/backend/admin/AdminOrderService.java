package com.ecommerce.backend.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.admin.dto.UpdateOrderStatusRequest;
import com.ecommerce.backend.order.Order;
import com.ecommerce.backend.order.OrderItem;
import com.ecommerce.backend.order.OrderRepository;
import com.ecommerce.backend.order.OrderStatus;
import com.ecommerce.backend.order.dto.OrderResponse;
import com.ecommerce.backend.product.ProductRepository;

@Service
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public AdminOrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // ==========================================
    // Get All Orders
    // ==========================================

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(
            Pageable pageable
    ) {

        return orderRepository
                .findAll(pageable)
                .map(this::toOrderResponse);
    }

    // ==========================================
    // Update Order Status
    // ==========================================

    @Transactional
    public OrderResponse updateOrderStatus(
            String orderNumber,
            UpdateOrderStatusRequest request
    ) {

        if (orderNumber == null ||
                orderNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Order number is required."
            );
        }

        if (request == null ||
                request.status() == null) {

            throw new IllegalArgumentException(
                    "Order status is required."
            );
        }

        Order order =
                orderRepository
                        .findByOrderNumber(
                                orderNumber.trim()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Order not found."
                                )
                        );

        OrderStatus currentStatus =
                order.getStatus();

        OrderStatus requestedStatus =
                request.status();

        // ======================================
        // No-op status update
        // ======================================

        if (currentStatus == requestedStatus) {

            return toOrderResponse(order);
        }

        // ======================================
        // Validate transition
        // ======================================

        validateStatusTransition(
                currentStatus,
                requestedStatus
        );

        // ======================================
        // Cancellation
        // ======================================
        //
        // Restore inventory BEFORE marking the
        // order as cancelled.
        //
        // Everything is inside one transaction.
        //
        // If anything fails, the complete operation
        // rolls back.
        //
        // ======================================

        if (requestedStatus ==
                OrderStatus.CANCELLED) {

            restoreOrderStock(order);
        }

        // ======================================
        // Apply new status
        // ======================================

        order.setStatus(
                requestedStatus
        );

        Order savedOrder =
                orderRepository.save(order);

        return toOrderResponse(
                savedOrder
        );
    }

    // ==========================================
    // Restore Stock For Cancelled Order
    // ==========================================

    private void restoreOrderStock(
            Order order
    ) {

        for (OrderItem item :
                order.getItems()) {

            Long productId =
                    item.getProductId();

            int quantity =
                    item.getQuantity();

            if (productId == null) {

                throw new IllegalArgumentException(
                        "Order contains an invalid product."
                );
            }

            if (quantity <= 0) {

                throw new IllegalArgumentException(
                        "Order contains an invalid quantity."
                );
            }

            int updatedRows =
                    productRepository.restoreStock(
                            productId,
                            quantity
                    );

            if (updatedRows != 1) {

                throw new IllegalArgumentException(
                        "Unable to restore stock for product ID: "
                                + productId
                );
            }
        }
    }

    // ==========================================
    // Validate Status Transition
    // ==========================================

    private void validateStatusTransition(
            OrderStatus currentStatus,
            OrderStatus requestedStatus
    ) {

        if (currentStatus == null) {

            throw new IllegalArgumentException(
                    "Current order status is invalid."
            );
        }

        // --------------------------------------
        // Cancelled orders are final
        // --------------------------------------

        if (currentStatus ==
                OrderStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Cancelled orders cannot be updated."
            );
        }

        // --------------------------------------
        // Delivered orders are final
        // --------------------------------------

        if (currentStatus ==
                OrderStatus.DELIVERED) {

            throw new IllegalArgumentException(
                    "Delivered orders cannot be updated."
            );
        }

        // --------------------------------------
        // Normal status flow
        // --------------------------------------

        boolean validTransition =
                switch (currentStatus) {

                    case PLACED ->
                            requestedStatus ==
                                    OrderStatus.CONFIRMED
                            ||
                            requestedStatus ==
                                    OrderStatus.CANCELLED;

                    case CONFIRMED ->
                            requestedStatus ==
                                    OrderStatus.SHIPPED
                            ||
                            requestedStatus ==
                                    OrderStatus.CANCELLED;

                    case SHIPPED ->
                            requestedStatus ==
                                    OrderStatus.DELIVERED;

                    default ->
                            false;
                };

        if (!validTransition) {

            throw new IllegalArgumentException(
                    "Invalid order status transition: "
                            + currentStatus
                            + " → "
                            + requestedStatus
            );
        }
    }

    // ==========================================
    // Convert Order → OrderResponse
    // ==========================================

    private OrderResponse toOrderResponse(
            Order order
    ) {

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),

                order.getFirstName(),
                order.getLastName(),

                order.getEmail(),
                order.getPhone(),

                order.getAddress(),
                order.getCity(),
                order.getState(),
                order.getPincode(),

                order.getSubtotal(),
                order.getDeliveryCharge(),
                order.getTotal(),

                order.getPaymentMethod(),

                order.getStatus(),

                order.getCreatedAt(),

                order.getItems()
                        .stream()
                        .map(this::toOrderItemResponse)
                        .toList()
        );
    }

    // ==========================================
    // Convert OrderItem → Response
    // ==========================================

    private OrderResponse.OrderItemResponse
    toOrderItemResponse(
            OrderItem item
    ) {

        return new OrderResponse.OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getProductSlug(),
                item.getProductImageUrl(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }
}