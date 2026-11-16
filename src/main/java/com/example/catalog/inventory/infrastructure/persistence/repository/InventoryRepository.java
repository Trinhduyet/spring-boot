package com.example.catalog.inventory.infrastructure.persistence.repository;

import com.example.catalog.inventory.infrastructure.persistence.entity.InventoryEntity;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryEntity i where i.productId = :id")
    Optional<InventoryEntity> lockByProductId(@Param("id") Long productId);
}
