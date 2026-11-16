package com.example.catalog.product.api.mapper;

import com.example.catalog.product.api.request.CreateProductRequest;
import com.example.catalog.product.api.request.UpdateProductRequest;
import com.example.catalog.product.api.response.ProductResponse;
import com.example.catalog.product.application.input.CreateProductInput;
import com.example.catalog.product.application.input.UpdateProductInput;
import com.example.catalog.product.application.result.ProductResult;

public final class ProductApiMapper {
    private ProductApiMapper() {}

    public static CreateProductInput toInput(CreateProductRequest value) {
        return new CreateProductInput(value.sku(), value.name(), value.price(), value.active());
    }

    public static UpdateProductInput toInput(UpdateProductRequest value) {
        return new UpdateProductInput(value.name(), value.price(), value.active());
    }

    public static ProductResponse toResponse(ProductResult value) {
        return new ProductResponse(
                value.id(),
                value.sku(),
                value.name(),
                value.price(),
                value.active(),
                value.version(),
                value.createdAt(),
                value.updatedAt());
    }
}
