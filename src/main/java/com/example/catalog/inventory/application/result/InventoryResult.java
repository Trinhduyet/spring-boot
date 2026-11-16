package com.example.catalog.inventory.application.result;

public record InventoryResult(
        Long productId, int onHand, int reserved, int available, long version) {}
