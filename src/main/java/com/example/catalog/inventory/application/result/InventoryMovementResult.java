package com.example.catalog.inventory.application.result;

import java.time.Instant;

public record InventoryMovementResult(
        Long id, Long productId, String type, int quantity, String reference, Instant createdAt) {}
