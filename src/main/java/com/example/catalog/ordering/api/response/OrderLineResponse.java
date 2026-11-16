package com.example.catalog.ordering.api.response;

import java.math.BigDecimal;

public record OrderLineResponse(
        Long productId,
        String sku,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal) {}
