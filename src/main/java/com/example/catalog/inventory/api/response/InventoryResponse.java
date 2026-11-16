package com.example.catalog.inventory.api.response;

public record InventoryResponse(
        Long productId, int onHand, int reserved, int available, long version) {}
