package com.ecommerce.backend.order;

public class InsufficientStockException extends RuntimeException {

    private final Long productId;
    private final String productName;
    private final int requestedQuantity;
    private final int availableQuantity;

    public InsufficientStockException(
            Long productId,
            String productName,
            int requestedQuantity,
            int availableQuantity
    ) {
        super(
                "Insufficient stock for product: "
                        + productName
                        + ". Requested: "
                        + requestedQuantity
                        + ", available: "
                        + availableQuantity
        );

        this.productId = productId;
        this.productName = productName;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}