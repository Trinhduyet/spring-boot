package com.example.catalog.category.application.mapper;

import com.example.catalog.category.application.result.CategoryResult;
import com.example.catalog.category.infrastructure.persistence.entity.CategoryEntity;

public final class CategoryResultMapper {
    private CategoryResultMapper() {}

    public static CategoryResult toResult(CategoryEntity entity) {
        return new CategoryResult(
                entity.getId(),
                entity.getName(),
                entity.getSlug(),
                entity.isActive(),
                entity.getVersion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
