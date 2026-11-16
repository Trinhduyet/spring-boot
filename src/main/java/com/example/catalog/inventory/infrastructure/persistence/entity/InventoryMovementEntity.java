package com.example.catalog.inventory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false, length = 24)
    private String type;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, length = 200)
    private String reference;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected InventoryMovementEntity() {}

    public InventoryMovementEntity(Long productId, String type, int quantity, String reference) {
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.reference = reference;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReference() {
        return reference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
