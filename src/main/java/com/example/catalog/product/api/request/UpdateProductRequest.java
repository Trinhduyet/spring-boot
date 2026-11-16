package com.example.catalog.product.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank @Size(max = 200) String name,
        @NotNull @DecimalMin("0.00") @Digits(integer = 12, fraction = 2) BigDecimal price,
        @NotNull Boolean active) {}
