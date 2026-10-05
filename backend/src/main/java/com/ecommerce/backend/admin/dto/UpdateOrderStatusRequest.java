package com.ecommerce.backend.admin.dto;

import com.ecommerce.backend.order.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(

        @NotNull(message = "Order status is required")
        OrderStatus status

) {}