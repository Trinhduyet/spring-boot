package com.example.catalog.inventory.domain.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(long id) {
        super("Insufficient available stock for product " + id);
    }
}
