package com.ecommerce.backend.order.dto;

import java.util.List;

import com.ecommerce.backend.order.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(

        // ==========================================
        // Customer Information
        // ==========================================

        @NotBlank(
                message = "First name is required"
        )
        @Size(
                max = 100,
                message = "First name is too long"
        )
        String firstName,

        @NotBlank(
                message = "Last name is required"
        )
        @Size(
                max = 100,
                message = "Last name is too long"
        )
        String lastName,

        @NotBlank(
                message = "Email is required"
        )
        @Email(
                message = "Invalid email address"
        )
        @Size(
                max = 255,
                message = "Email is too long"
        )
        String email,

        @NotBlank(
                message = "Phone number is required"
        )
        @Pattern(
                regexp = "^[6-9][0-9]{9}$",
                message = "Invalid Indian mobile number"
        )
        String phone,

        // ==========================================
        // Delivery Address
        // ==========================================

        @NotBlank(
                message = "Address is required"
        )
        @Size(
                max = 500,
                message = "Address is too long"
        )
        String address,

        @NotBlank(
                message = "City is required"
        )
        @Size(
                max = 100,
                message = "City is too long"
        )
        String city,

        @NotBlank(
                message = "State is required"
        )
        @Size(
                max = 100,
                message = "State is too long"
        )
        String state,

        @NotBlank(
                message = "Pincode is required"
        )
        @Pattern(
                regexp = "^[1-9][0-9]{5}$",
                message = "Invalid Indian pincode"
        )
        String pincode,

        // ==========================================
        // Payment
        // ==========================================

        @NotNull(
                message = "Payment method is required"
        )
        PaymentMethod paymentMethod,

        // ==========================================
        // Order Items
        // ==========================================

        @NotEmpty(
                message = "Order must contain at least one item"
        )
        List<
                @Valid
                OrderItemRequest
        > items
) {
}