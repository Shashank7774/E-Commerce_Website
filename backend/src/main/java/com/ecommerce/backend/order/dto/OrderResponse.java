package com.ecommerce.backend.order.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.ecommerce.backend.order.OrderStatus;
import com.ecommerce.backend.order.PaymentMethod;

public record OrderResponse(

        Long id,

        String orderNumber,

        String firstName,

        String lastName,

        String email,

        String phone,

        String address,

        String city,

        String state,

        String pincode,

        BigDecimal subtotal,

        BigDecimal deliveryCharge,

        BigDecimal total,

        PaymentMethod paymentMethod,

        OrderStatus status,

        OffsetDateTime createdAt,

        List<OrderItemResponse> items
) {

    public record OrderItemResponse(

            Long productId,

            String productName,

            String productSlug,

            String productImageUrl,

            BigDecimal unitPrice,

            Integer quantity,

            BigDecimal lineTotal
    ) {
    }
}