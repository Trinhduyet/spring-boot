package com.example.catalog.inventory.api.mapper;

import com.example.catalog.inventory.api.response.InventoryMovementResponse;
import com.example.catalog.inventory.api.response.InventoryResponse;
import com.example.catalog.inventory.application.result.InventoryMovementResult;
import com.example.catalog.inventory.application.result.InventoryResult;

public final class InventoryApiMapper {
    private InventoryApiMapper() {}

    public static InventoryMovementResponse toResponse(InventoryMovementResult value) {
        return new InventoryMovementResponse(
                value.id(),
                value.productId(),
                value.type(),
                value.quantity(),
                value.reference(),
                value.createdAt());
    }

    public static InventoryResponse toResponse(InventoryResult value) {
        return new InventoryResponse(
                value.productId(),
                value.onHand(),
                value.reserved(),
                value.available(),
                value.version());
    }
}
