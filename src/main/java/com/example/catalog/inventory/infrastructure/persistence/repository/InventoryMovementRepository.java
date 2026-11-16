package com.example.catalog.inventory.infrastructure.persistence.repository;

import com.example.catalog.inventory.infrastructure.persistence.entity.InventoryMovementEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovementEntity, Long> {
    List<InventoryMovementEntity> findTop100ByProductIdOrderByIdDesc(Long productId);
}
