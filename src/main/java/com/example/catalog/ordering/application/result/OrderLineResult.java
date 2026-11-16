package com.example.catalog.ordering.application.result;

import java.math.BigDecimal;

public record OrderLineResult(
        Long productId,
        String sku,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal) {}
