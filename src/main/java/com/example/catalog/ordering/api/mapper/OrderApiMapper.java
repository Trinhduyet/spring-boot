package com.example.catalog.ordering.api.mapper;

import com.example.catalog.ordering.api.request.CreateOrderRequest;
import com.example.catalog.ordering.api.request.OrderLineRequest;
import com.example.catalog.ordering.api.response.OrderLineResponse;
import com.example.catalog.ordering.api.response.OrderResponse;
import com.example.catalog.ordering.application.input.CreateOrderInput;
import com.example.catalog.ordering.application.input.OrderLineInput;
import com.example.catalog.ordering.application.result.OrderLineResult;
import com.example.catalog.ordering.application.result.OrderResult;

public final class OrderApiMapper {
    private OrderApiMapper() {}

    public static CreateOrderInput toInput(CreateOrderRequest value) {
        return new CreateOrderInput(value.lines().stream().map(OrderApiMapper::toInput).toList());
    }

    public static OrderLineInput toInput(OrderLineRequest value) {
        return new OrderLineInput(value.productId(), value.quantity());
    }

    public static OrderLineResponse toResponse(OrderLineResult value) {
        return new OrderLineResponse(
                value.productId(),
                value.sku(),
                value.productName(),
                value.quantity(),
                value.unitPrice(),
                value.lineTotal());
    }

    public static OrderResponse toResponse(OrderResult value) {
        return new OrderResponse(
                value.id(),
                value.status(),
                value.lines().stream().map(OrderApiMapper::toResponse).toList(),
                value.total(),
                value.createdAt(),
                value.updatedAt());
    }
}
