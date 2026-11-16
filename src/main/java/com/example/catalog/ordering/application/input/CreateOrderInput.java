package com.example.catalog.ordering.application.input;

import java.util.List;

public record CreateOrderInput(List<OrderLineInput> lines) {}
