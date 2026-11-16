package com.example.catalog.inventory.api.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryAdjustmentRequest(
        @NotNull @Min(-1000000) @Max(1000000) Integer quantity, String reference) {}
