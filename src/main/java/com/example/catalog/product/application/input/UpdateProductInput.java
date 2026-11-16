package com.example.catalog.product.application.input;

import java.math.BigDecimal;

public record UpdateProductInput(String name, BigDecimal price, Boolean active) {}
