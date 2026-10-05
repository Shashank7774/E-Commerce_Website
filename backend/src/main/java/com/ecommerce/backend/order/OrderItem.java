package com.ecommerce.backend.order;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

    // ==========================================
    // Primary Key
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // Parent Order
    // ==========================================

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Order order;

    // ==========================================
    // Product Reference
    // ==========================================

    @Column(
            name = "product_id",
            nullable = false
    )
    private Long productId;

    // ==========================================
    // Product Snapshot
    // ==========================================

    @Column(
            name = "product_name",
            nullable = false,
            length = 200
    )
    private String productName;

    @Column(
            name = "product_slug",
            nullable = false,
            length = 150
    )
    private String productSlug;

    @Column(
            name = "product_image_url",
            nullable = false,
            length = 500
    )
    private String productImageUrl;

    // ==========================================
    // Purchase Price Snapshot
    // ==========================================

    @Column(
            name = "unit_price",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal unitPrice;

    // ==========================================
    // Quantity
    // ==========================================

    @Column(
            nullable = false
    )
    private Integer quantity;

    // ==========================================
    // Line Total
    // ==========================================

    @Column(
            name = "line_total",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal lineTotal;

    // ==========================================
    // Constructor
    // ==========================================

    public OrderItem() {
    }

    // ==========================================
    // Getters
    // ==========================================

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    // ==========================================
    // Setters
    // ==========================================

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(
            String productName
    ) {
        this.productName = productName;
    }

    public void setProductSlug(
            String productSlug
    ) {
        this.productSlug = productSlug;
    }

    public void setProductImageUrl(
            String productImageUrl
    ) {
        this.productImageUrl = productImageUrl;
    }

    public void setUnitPrice(
            BigDecimal unitPrice
    ) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(
            Integer quantity
    ) {
        this.quantity = quantity;
    }

    public void setLineTotal(
            BigDecimal lineTotal
    ) {
        this.lineTotal = lineTotal;
    }
}