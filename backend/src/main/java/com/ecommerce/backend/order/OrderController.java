package com.ecommerce.backend.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.order.dto.CreateOrderRequest;
import com.ecommerce.backend.order.dto.OrderResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(
        origins = "http://localhost:3000",
        allowCredentials = "true"
)
public class OrderController {

    private final OrderService orderService;

    // ==========================================
    // Constructor
    // ==========================================

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    // ==========================================
    // Create Order
    // ==========================================

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid
            @RequestBody
            CreateOrderRequest request,
            Authentication authentication
    ) {

        String authenticatedEmail =
                authentication.getName();

        OrderResponse response =
                orderService.createOrder(
                        request,
                        authenticatedEmail
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==========================================
    // Get My Orders
    // ==========================================

    @GetMapping("/my-orders")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            Authentication authentication,
            Pageable pageable
    ) {

        String authenticatedEmail =
                authentication.getName();

        Page<OrderResponse> response =
                orderService.getMyOrders(
                        authenticatedEmail,
                        pageable
                );

        return ResponseEntity.ok(
                response
        );
    }

    // ==========================================
    // Get Order by Order Number
    // ==========================================

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderResponse>
    getOrderByOrderNumber(
            @PathVariable String orderNumber,
            Authentication authentication
    ) {

        String authenticatedEmail =
                authentication.getName();

        OrderResponse response =
                orderService.getOrderByOrderNumber(
                        orderNumber,
                        authenticatedEmail
                );

        return ResponseEntity.ok(
                response
        );
    }
}