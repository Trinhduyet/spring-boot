package com.example.catalog.category.domain.exception;

public class CategoryInUseException extends RuntimeException {
    public CategoryInUseException(long id) {
        super("Category " + id + " is assigned to one or more products");
    }
}
