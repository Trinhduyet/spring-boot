package com.example.catalog.ordering.infrastructure.persistence.entity;

import com.example.catalog.ordering.domain.exception.InvalidOrderStateException;
import com.example.catalog.ordering.domain.model.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import java.time.Instant;

@Entity
@Table(
        name = "customer_orders",
        uniqueConstraints =
                @UniqueConstraint(
                        name = "ux_order_actor_idempotency",
                        columnNames = {"actor_id", "idempotency_key"}))
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actor_id", nullable = false, length = 200)
    private String actorId;

    @Column(name = "idempotency_key", nullable = false, length = 128)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private long version;

    protected OrderEntity() {}

    public OrderEntity(String actorId, String idempotencyKey) {
        this.actorId = actorId;
        this.idempotencyKey = idempotencyKey;
        this.status = OrderStatus.RESERVED;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public void cancel() {
        if (status != OrderStatus.RESERVED) throw new InvalidOrderStateException(id);
        status = OrderStatus.CANCELLED;
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getActorId() {
        return actorId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
