package com.ecommerce.backend.user;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = "email"
                )
        }
)
public class User {

    // ==========================================
    // Primary Key
    // ==========================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ==========================================
    // Basic Information
    // ==========================================

    @Column(
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            nullable = false,
            length = 100
    )
    private String lastName;

    @Column(
            nullable = false,
            unique = true,
            length = 255
    )
    private String email;

    @Column(
            nullable = false,
            length = 20
    )
    private String phone;

    // ==========================================
    // Authentication
    // ==========================================

    @Column(
            name = "password_hash",
            nullable = false,
            length = 255
    )
    private String passwordHash;

    // ==========================================
    // Authorization
    // ==========================================

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private UserRole role;

    // ==========================================
    // Account Status
    // ==========================================

    @Column(
            nullable = false
    )
    private boolean enabled;

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

    public User() {
    }

    // ==========================================
    // Getters
    // ==========================================

    public Long getId() {
        return id;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
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

    public void setPasswordHash(
            String passwordHash
    ) {
        this.passwordHash = passwordHash;
    }

    public void setRole(
            UserRole role
    ) {
        this.role = role;
    }

    public void setEnabled(
            boolean enabled
    ) {
        this.enabled = enabled;
    }

    // ==========================================
    // Lifecycle
    // ==========================================

    @PrePersist
    protected void onCreate() {

        OffsetDateTime now =
                OffsetDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (role == null) {
            role = UserRole.CUSTOMER;
        }

        enabled = true;
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                OffsetDateTime.now();
    }
}