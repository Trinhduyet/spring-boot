package com.example.catalog.category.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank @Size(max = 80) String name,
        @NotBlank @Size(max = 96) @Pattern(regexp = "[A-Za-z0-9-]+") String slug,
        boolean active) {}
