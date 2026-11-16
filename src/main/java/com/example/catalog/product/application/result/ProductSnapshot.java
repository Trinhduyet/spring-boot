package com.example.catalog.product.application.result;

import java.math.BigDecimal;

public record ProductSnapshot(Long id, String sku, String name, BigDecimal price, boolean active) {}
