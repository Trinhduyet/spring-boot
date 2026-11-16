package com.example.catalog.category.application.result;

import java.time.Instant;

public record CategoryResult(
        Long id,
        String name,
        String slug,
        boolean active,
        long version,
        Instant createdAt,
        Instant updatedAt) {}
