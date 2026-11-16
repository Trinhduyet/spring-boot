package com.example.catalog.category.domain.exception;

public class CategoryConflictException extends RuntimeException {
    public CategoryConflictException(String slug) {
        super("Category slug already exists: " + slug);
    }
}
