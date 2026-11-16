package com.example.catalog.category.api.mapper;

import com.example.catalog.category.api.request.CategoryRequest;
import com.example.catalog.category.api.response.CategoryResponse;
import com.example.catalog.category.application.input.CategoryInput;
import com.example.catalog.category.application.result.CategoryResult;

public final class CategoryApiMapper {
    private CategoryApiMapper() {}

    public static CategoryInput toInput(CategoryRequest value) {
        return new CategoryInput(value.name(), value.slug(), value.active());
    }

    public static CategoryResponse toResponse(CategoryResult value) {
        return new CategoryResponse(
                value.id(),
                value.name(),
                value.slug(),
                value.active(),
                value.version(),
                value.createdAt(),
                value.updatedAt());
    }
}
