package com.example.catalog.ordering.infrastructure.persistence.repository;

import com.example.catalog.ordering.infrastructure.persistence.entity.OrderEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByActorIdAndIdempotencyKey(String actorId, String key);

    Optional<OrderEntity> findByIdAndActorId(Long id, String actorId);
}
