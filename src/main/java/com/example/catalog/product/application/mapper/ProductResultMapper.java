package com.example.catalog.product.application.mapper;

import com.example.catalog.product.application.result.ProductResult;
import com.example.catalog.product.infrastructure.persistence.entity.ProductEntity;

public final class ProductResultMapper {
    private ProductResultMapper() {}

    public static ProductResult toResult(ProductEntity product) {
        return new ProductResult(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getPrice(),
                product.isActive(),
                product.getVersion(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}
