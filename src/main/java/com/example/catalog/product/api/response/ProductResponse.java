package com.example.catalog.product.api.response;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        BigDecimal price,
        boolean active,
        long version,
        Instant createdAt,
        Instant updatedAt) {}
