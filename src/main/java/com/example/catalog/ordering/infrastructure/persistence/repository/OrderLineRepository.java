package com.example.catalog.ordering.infrastructure.persistence.repository;

import com.example.catalog.ordering.infrastructure.persistence.entity.OrderLineEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLineEntity, Long> {
    List<OrderLineEntity> findByOrderId(Long orderId);
}
