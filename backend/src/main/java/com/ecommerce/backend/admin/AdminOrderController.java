package com.ecommerce.backend.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.admin.dto.UpdateOrderStatusRequest;
import com.ecommerce.backend.order.dto.OrderResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    public AdminOrderController(
            AdminOrderService adminOrderService
    ) {
        this.adminOrderService =
                adminOrderService;
    }

    // ==========================================
    // Get All Orders
    // ==========================================

    @GetMapping
    public ResponseEntity<Page<OrderResponse>>
    getAllOrders(
            Pageable pageable
    ) {

        Page<OrderResponse> orders =
                adminOrderService.getAllOrders(
                        pageable
                );

        return ResponseEntity.ok(orders);
    }

    // ==========================================
    // Update Order Status
    // ==========================================

    @PatchMapping("/{orderNumber}/status")
    public ResponseEntity<OrderResponse>
    updateOrderStatus(
            @PathVariable String orderNumber,

            @Valid
            @RequestBody
            UpdateOrderStatusRequest request
    ) {

        OrderResponse updatedOrder =
                adminOrderService.updateOrderStatus(
                        orderNumber,
                        request
                );

        return ResponseEntity.ok(
                updatedOrder
        );
    }
}