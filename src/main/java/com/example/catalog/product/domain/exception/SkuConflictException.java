package com.example.catalog.product.domain.exception;

public class SkuConflictException extends RuntimeException {
    public SkuConflictException(String sku) {
        super("Product SKU already exists: " + sku);
    }
}
