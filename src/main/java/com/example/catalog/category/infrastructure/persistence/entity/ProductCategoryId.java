package com.example.catalog.category.infrastructure.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

public class ProductCategoryId implements Serializable {
    public Long productId;
    public Long categoryId;

    public ProductCategoryId() {}

    public ProductCategoryId(Long productId, Long categoryId) {
        this.productId = productId;
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object value) {
        return value instanceof ProductCategoryId other
                && Objects.equals(productId, other.productId)
                && Objects.equals(categoryId, other.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, categoryId);
    }
}
