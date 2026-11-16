package com.example.catalog.inventory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;

@Entity
@Table(name = "inventory")
public class InventoryEntity {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "on_hand", nullable = false)
    private int onHand;

    @Column(nullable = false)
    private int reserved;

    @Version
    @Column(nullable = false)
    private long version;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected InventoryEntity() {}

    public InventoryEntity(Long productId, int initialOnHand) {
        this.productId = productId;
        this.onHand = initialOnHand;
        this.reserved = 0;
        this.updatedAt = Instant.now();
    }

    public Long getProductId() {
        return productId;
    }

    public int getOnHand() {
        return onHand;
    }

    public int getReserved() {
        return reserved;
    }

    public int getAvailable() {
        return onHand - reserved;
    }

    public long getVersion() {
        return version;
    }
}
