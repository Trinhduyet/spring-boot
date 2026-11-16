package com.example.catalog.category.domain.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(long id) {
        super("Category " + id + " not found");
    }
}
