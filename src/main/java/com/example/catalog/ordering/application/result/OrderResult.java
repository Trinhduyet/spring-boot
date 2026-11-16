package com.example.catalog.ordering.application.result;

import com.example.catalog.ordering.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResult(
        Long id,
        OrderStatus status,
        List<OrderLineResult> lines,
        BigDecimal total,
        Instant createdAt,
        Instant updatedAt) {}
