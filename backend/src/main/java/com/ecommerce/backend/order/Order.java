package com.ecommerce.backend.order;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.backend.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    // ==========================================
    // Primary Key
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // Public Order Number
    // ==========================================

    @Column(
            name = "order_number",
            nullable = false,
            unique = true,
            length = 50
    )
    private String orderNumber;

    // ==========================================
    // Authenticated Customer
    // ==========================================

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "user_id",
            nullable = true
    )
    private User user;

    // ==========================================
    // Customer Information
    // ==========================================

    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    private String lastName;

    @Column(
            nullable = false,
            length = 255
    )
    private String email;

    @Column(
            nullable = false,
            length = 20
    )
    private String phone;

    // ==========================================
    // Delivery Address
    // ==========================================

    @Column(
            nullable = false,
            length = 500
    )
    private String address;

    @Column(
            nullable = false,
            length = 100
    )
    private String city;

    @Column(
            nullable = false,
            length = 100
    )
    private String state;

    @Column(
            nullable = false,
            length = 10
    )
    private String pincode;

    // ==========================================
    // Pricing
    // ==========================================

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal subtotal;

    @Column(
            name = "delivery_charge",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal deliveryCharge;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal total;

    // ==========================================
    // Payment
    // ==========================================

    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_method",
            nullable = false,
            length = 30
    )
    private PaymentMethod paymentMethod;

    // ==========================================
    // Order Status
    // ==========================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private OrderStatus status;

    // ==========================================
    // Order Items
    // ==========================================

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items =
            new ArrayList<>();

    // ==========================================
    // Timestamps
    // ==========================================

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private OffsetDateTime updatedAt;

    // ==========================================
    // Constructor
    // ==========================================

    public Order() {
    }

    // ==========================================
    // Getters
    // ==========================================

    public Long getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public User getUser() {
        return user;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDeliveryCharge() {
        return deliveryCharge;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ==========================================
    // Setters
    // ==========================================

    public void setOrderNumber(
            String orderNumber
    ) {
        this.orderNumber = orderNumber;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setFirstName(
            String firstName
    ) {
        this.firstName = firstName;
    }

    public void setLastName(
            String lastName
    ) {
        this.lastName = lastName;
    }

    public void setEmail(
            String email
    ) {
        this.email = email;
    }

    public void setPhone(
            String phone
    ) {
        this.phone = phone;
    }

    public void setAddress(
            String address
    ) {
        this.address = address;
    }

    public void setCity(
            String city
    ) {
        this.city = city;
    }

    public void setState(
            String state
    ) {
        this.state = state;
    }

    public void setPincode(
            String pincode
    ) {
        this.pincode = pincode;
    }

    public void setSubtotal(
            BigDecimal subtotal
    ) {
        this.subtotal = subtotal;
    }

    public void setDeliveryCharge(
            BigDecimal deliveryCharge
    ) {
        this.deliveryCharge =
                deliveryCharge;
    }

    public void setTotal(
            BigDecimal total
    ) {
        this.total = total;
    }

    public void setPaymentMethod(
            PaymentMethod paymentMethod
    ) {
        this.paymentMethod =
                paymentMethod;
    }

    public void setStatus(
            OrderStatus status
    ) {
        this.status = status;
    }

    public void setItems(
            List<OrderItem> items
    ) {
        this.items = items;
    }

    // ==========================================
    // Add Order Item
    // ==========================================

    public void addItem(OrderItem item) {

        items.add(item);

        item.setOrder(this);
    }

    // ==========================================
    // Remove Order Item
    // ==========================================

    public void removeItem(OrderItem item) {

        items.remove(item);

        item.setOrder(null);
    }

    // ==========================================
    // Lifecycle - Before Insert
    // ==========================================

    @PrePersist
    protected void onCreate() {

        OffsetDateTime now =
                OffsetDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = OrderStatus.PLACED;
        }
    }

    // ==========================================
    // Lifecycle - Before Update
    // ==========================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = OffsetDateTime.now();
    }
}