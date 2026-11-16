package com.example.catalog.inventory.application.mapper;

import com.example.catalog.inventory.application.result.InventoryMovementResult;
import com.example.catalog.inventory.application.result.InventoryResult;
import com.example.catalog.inventory.infrastructure.persistence.entity.InventoryEntity;
import com.example.catalog.inventory.infrastructure.persistence.entity.InventoryMovementEntity;

public final class InventoryResultMapper {
    private InventoryResultMapper() {}

    public static InventoryResult toResult(InventoryEntity entity) {
        return new InventoryResult(
                entity.getProductId(),
                entity.getOnHand(),
                entity.getReserved(),
                entity.getAvailable(),
                entity.getVersion());
    }

    public static InventoryMovementResult toMovementResult(InventoryMovementEntity entity) {
        return new InventoryMovementResult(
                entity.getId(),
                entity.getProductId(),
                entity.getType(),
                entity.getQuantity(),
                entity.getReference(),
                entity.getCreatedAt());
    }
}
