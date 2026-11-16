package com.example.catalog.ordering.application.mapper;

import com.example.catalog.ordering.application.result.OrderLineResult;
import com.example.catalog.ordering.application.result.OrderResult;
import com.example.catalog.ordering.infrastructure.persistence.entity.OrderEntity;
import com.example.catalog.ordering.infrastructure.persistence.entity.OrderLineEntity;

import java.math.BigDecimal;
import java.util.List;

public final class OrderResultMapper {
    private OrderResultMapper() {}

    public static OrderResult toResult(OrderEntity order, List<OrderLineEntity> orderLines) {
        List<OrderLineResult> details =
                orderLines.stream().map(OrderResultMapper::toLineResult).toList();
        BigDecimal total =
                details.stream()
                        .map(OrderLineResult::lineTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new OrderResult(
                order.getId(),
                order.getStatus(),
                details,
                total,
                order.getCreatedAt(),
                order.getUpdatedAt());
    }

    public static OrderLineResult toLineResult(OrderLineEntity line) {
        BigDecimal lineTotal = line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity()));
        return new OrderLineResult(
                line.getProductId(),
                line.getSku(),
                line.getProductName(),
                line.getQuantity(),
                line.getUnitPrice(),
                lineTotal);
    }
}
