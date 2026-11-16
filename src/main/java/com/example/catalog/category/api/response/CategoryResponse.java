package com.example.catalog.category.api.response;

import java.time.Instant;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        boolean active,
        long version,
        Instant createdAt,
        Instant updatedAt) {}
