package com.example.catalog.product.application.result;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResult(
        Long id,
        String sku,
        String name,
        BigDecimal price,
        boolean active,
        long version,
        Instant createdAt,
        Instant updatedAt) {}
