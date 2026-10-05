package com.ecommerce.backend.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.order.dto.CreateOrderRequest;
import com.ecommerce.backend.order.dto.OrderItemRequest;
import com.ecommerce.backend.order.dto.OrderResponse;
import com.ecommerce.backend.product.Product;
import com.ecommerce.backend.product.ProductRepository;
import com.ecommerce.backend.user.User;
import com.ecommerce.backend.user.UserRepository;

@Service
public class OrderService {

    // ==========================================
    // Delivery Configuration
    // ==========================================

    private static final BigDecimal FREE_DELIVERY_THRESHOLD =
            new BigDecimal("999.00");

    private static final BigDecimal DELIVERY_CHARGE =
            new BigDecimal("49.00");

    // ==========================================
    // Repositories
    // ==========================================

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    // ==========================================
    // Constructor
    // ==========================================

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ==========================================
    // Create Order
    // ==========================================

    @Transactional
    public OrderResponse createOrder(
            CreateOrderRequest request,
            String authenticatedEmail
    ) {

        // --------------------------------------
        // Validate authenticated user
        // --------------------------------------

        if (
                authenticatedEmail == null ||
                authenticatedEmail.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Authenticated user is required"
            );
        }

        String normalizedEmail =
                authenticatedEmail
                        .trim()
                        .toLowerCase();

        User user =
                userRepository
                        .findByEmail(normalizedEmail)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Authenticated user not found"
                                        )
                        );

        // --------------------------------------
        // Validate order items
        // --------------------------------------

        if (
                request.items() == null ||
                request.items().isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "Order must contain at least one item"
            );
        }

        List<OrderItemRequest> items =
                validateItems(request.items());

        // --------------------------------------
        // Create Order Entity
        // --------------------------------------

        Order order = new Order();

        order.setOrderNumber(
                generateUniqueOrderNumber()
        );

        // --------------------------------------
        // Associate authenticated user
        // --------------------------------------

        order.setUser(user);

        // --------------------------------------
        // Customer Information
        // --------------------------------------

        order.setFirstName(
                requireUserValue(
                        user.getFirstName(),
                        "User first name is missing"
                )
        );

        order.setLastName(
                requireUserValue(
                        user.getLastName(),
                        "User last name is missing"
                )
        );

        order.setEmail(
                requireUserValue(
                        user.getEmail(),
                        "User email is missing"
                )
                        .trim()
                        .toLowerCase()
        );

        order.setPhone(
                requireUserValue(
                        user.getPhone(),
                        "User phone number is missing"
                ).trim()
        );

        // --------------------------------------
        // Delivery Address
        // --------------------------------------

        order.setAddress(
                request.address().trim()
        );

        order.setCity(
                request.city().trim()
        );

        order.setState(
                request.state().trim()
        );

        order.setPincode(
                request.pincode().trim()
        );

        // --------------------------------------
        // Payment
        // --------------------------------------

        order.setPaymentMethod(
                request.paymentMethod()
        );

        order.setStatus(
                OrderStatus.PLACED
        );

        // --------------------------------------
        // Calculate subtotal
        // --------------------------------------

        BigDecimal subtotal =
                BigDecimal.ZERO;

        // --------------------------------------
        // Process each product
        // --------------------------------------

        for (
                OrderItemRequest itemRequest
                : items
        ) {

            Product product =
                    productRepository
                            .findById(
                                    itemRequest.productId()
                            )
                            .orElseThrow(
                                    () ->
                                            new IllegalArgumentException(
                                                    "Product not found: "
                                                            + itemRequest.productId()
                                            )
                            );

            // ----------------------------------
            // Validate quantity
            // ----------------------------------

            int requestedQuantity =
                    itemRequest.quantity();

            if (requestedQuantity <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be greater than zero"
                );
            }

            // ----------------------------------
            // Use actual database price
            // ----------------------------------

            BigDecimal unitPrice =
                    product.getPrice();

            if (unitPrice == null) {

                throw new IllegalArgumentException(
                        "Product price is unavailable: "
                                + product.getName()
                );
            }

            // ----------------------------------
            // Atomically reserve/decrement stock
            // ----------------------------------
            //
            // Database performs:
            //
            // stock = stock - requestedQuantity
            //
            // only when:
            //
            // stock >= requestedQuantity
            //
            // 1 = success
            // 0 = insufficient stock
            //

            int updatedRows =
                    productRepository
                            .decrementStockIfAvailable(
                                    product.getId(),
                                    requestedQuantity
                            );

            if (updatedRows == 0) {

                /*
                 * IMPORTANT:
                 *
                 * The atomic UPDATE tells us that
                 * requested quantity was not available.
                 *
                 * We now read the current database
                 * stock so the frontend can show the
                 * user the actual available quantity.
                 */
                int availableQuantity =
                        productRepository
                                .findById(
                                        product.getId()
                                )
                                .map(Product::getStock)
                                .orElse(0);

                throw new InsufficientStockException(
                        product.getId(),
                        product.getName(),
                        requestedQuantity,
                        availableQuantity
                );
            }

            // ----------------------------------
            // Calculate line total
            // ----------------------------------

            BigDecimal lineTotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    requestedQuantity
                            )
                    );

            // ----------------------------------
            // Create OrderItem snapshot
            // ----------------------------------

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setProductId(
                    product.getId()
            );

            orderItem.setProductName(
                    product.getName()
            );

            orderItem.setProductSlug(
                    product.getSlug()
            );

            orderItem.setProductImageUrl(
                    product.getImageUrl()
            );

            orderItem.setUnitPrice(
                    unitPrice
            );

            orderItem.setQuantity(
                    requestedQuantity
            );

            orderItem.setLineTotal(
                    lineTotal
            );

            // ----------------------------------
            // Attach item to order
            // ----------------------------------

            order.addItem(orderItem);

            // ----------------------------------
            // Add line total to subtotal
            // ----------------------------------

            subtotal =
                    subtotal.add(lineTotal);
        }

        // --------------------------------------
        // Calculate delivery charge
        // --------------------------------------

        BigDecimal deliveryCharge;

        if (
                subtotal.compareTo(
                        FREE_DELIVERY_THRESHOLD
                ) >= 0
        ) {

            deliveryCharge =
                    BigDecimal.ZERO;

        } else {

            deliveryCharge =
                    DELIVERY_CHARGE;
        }

        // --------------------------------------
        // Calculate final total
        // --------------------------------------

        BigDecimal total =
                subtotal.add(
                        deliveryCharge
                );

        // --------------------------------------
        // Set pricing
        // --------------------------------------

        order.setSubtotal(
                subtotal
        );

        order.setDeliveryCharge(
                deliveryCharge
        );

        order.setTotal(
                total
        );

        // --------------------------------------
        // Save Order
        // --------------------------------------

        Order savedOrder =
                orderRepository.save(order);

        // --------------------------------------
        // Convert Entity → Response
        // --------------------------------------

        return toOrderResponse(
                savedOrder
        );
    }

    // ==========================================
    // Get My Orders
    // ==========================================

    @Transactional(readOnly = true)
    public Page<OrderResponse> getMyOrders(
            String authenticatedEmail,
            Pageable pageable
    ) {

        if (
                authenticatedEmail == null ||
                authenticatedEmail.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Authenticated user is required"
            );
        }

        String normalizedEmail =
                authenticatedEmail
                        .trim()
                        .toLowerCase();

        User user =
                userRepository
                        .findByEmail(normalizedEmail)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Authenticated user not found"
                                        )
                        );

        Page<Order> orders =
                orderRepository
                        .findByUserIdOrderByCreatedAtDesc(
                                user.getId(),
                                pageable
                        );

        return orders.map(
                this::toOrderResponse
        );
    }

    // ==========================================
    // Get Order by Order Number
    // ==========================================

    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(
            String orderNumber,
            String authenticatedEmail
    ) {

        if (
                orderNumber == null ||
                orderNumber.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Order number is required"
            );
        }

        if (
                authenticatedEmail == null ||
                authenticatedEmail.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Authenticated user is required"
            );
        }

        String normalizedOrderNumber =
                orderNumber.trim();

        String normalizedEmail =
                authenticatedEmail
                        .trim()
                        .toLowerCase();

        User user =
                userRepository
                        .findByEmail(normalizedEmail)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Authenticated user not found"
                                        )
                        );

        Order order =
                orderRepository
                        .findByOrderNumberAndUserId(
                                normalizedOrderNumber,
                                user.getId()
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Order not found"
                                        )
                        );

        return toOrderResponse(order);
    }

    // ==========================================
    // Validate Order Items
    // ==========================================

    private List<OrderItemRequest> validateItems(
            List<OrderItemRequest> items
    ) {

        List<OrderItemRequest> validatedItems =
                new ArrayList<>();

        for (
                OrderItemRequest item
                : items
        ) {

            if (item == null) {

                throw new IllegalArgumentException(
                        "Order item cannot be null"
                );
            }

            if (item.productId() == null) {

                throw new IllegalArgumentException(
                        "Product ID is required"
                );
            }

            if (
                    item.quantity() == null ||
                    item.quantity() <= 0
            ) {

                throw new IllegalArgumentException(
                        "Quantity must be greater than zero"
                );
            }

            boolean duplicate =
                    validatedItems
                            .stream()
                            .anyMatch(
                                    existing ->
                                            existing
                                                    .productId()
                                                    .equals(
                                                            item.productId()
                                                    )
                            );

            if (duplicate) {

                throw new IllegalArgumentException(
                        "Duplicate product in order: "
                                + item.productId()
                );
            }

            validatedItems.add(item);
        }

        return validatedItems;
    }

    // ==========================================
    // Validate User Value
    // ==========================================

    private String requireUserValue(
            String value,
            String errorMessage
    ) {

        if (
                value == null ||
                value.isBlank()
        ) {
            throw new IllegalArgumentException(
                    errorMessage
            );
        }

        return value.trim();
    }

    // ==========================================
    // Generate Unique Order Number
    // ==========================================

    private String generateUniqueOrderNumber() {

        String orderNumber;

        do {

            String date =
                    LocalDate.now()
                            .format(
                                    DateTimeFormatter
                                            .BASIC_ISO_DATE
                            );

            String randomPart =
                    UUID.randomUUID()
                            .toString()
                            .substring(
                                    0,
                                    8
                            )
                            .toUpperCase();

            orderNumber =
                    "YS-"
                            + date
                            + "-"
                            + randomPart;

        } while (
                orderRepository
                        .existsByOrderNumber(
                                orderNumber
                        )
        );

        return orderNumber;
    }

    // ==========================================
    // Convert Order Entity → Response DTO
    // ==========================================

    private OrderResponse toOrderResponse(
            Order order
    ) {

        List<OrderResponse.OrderItemResponse>
                itemResponses =
                order.getItems()
                        .stream()
                        .map(
                                item ->
                                        new OrderResponse
                                                .OrderItemResponse(
                                                        item.getProductId(),
                                                        item.getProductName(),
                                                        item.getProductSlug(),
                                                        item.getProductImageUrl(),
                                                        item.getUnitPrice(),
                                                        item.getQuantity(),
                                                        item.getLineTotal()
                                                )
                        )
                        .toList();

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
                itemResponses
        );
    }
}