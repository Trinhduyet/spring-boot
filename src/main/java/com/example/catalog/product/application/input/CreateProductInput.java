package com.example.catalog.product.application.input;

import java.math.BigDecimal;

public record CreateProductInput(String sku, String name, BigDecimal price, Boolean active) {}
