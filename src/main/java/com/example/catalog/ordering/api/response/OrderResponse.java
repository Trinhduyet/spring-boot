package com.example.catalog.ordering.api.response;

import com.example.catalog.ordering.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        List<OrderLineResponse> lines,
        BigDecimal total,
        Instant createdAt,
        Instant updatedAt) {}
