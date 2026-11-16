package com.example.catalog.category.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "product_categories")
@IdClass(ProductCategoryId.class)
public class ProductCategoryEntity {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ProductCategoryEntity() {}

    public ProductCategoryEntity(Long productId, Long categoryId) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.createdAt = Instant.now();
    }

    public Long getProductId() {
        return productId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
