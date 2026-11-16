package com.example.catalog.inventory.api.response;

import java.time.Instant;

public record InventoryMovementResponse(
        Long id, Long productId, String type, int quantity, String reference, Instant createdAt) {}
