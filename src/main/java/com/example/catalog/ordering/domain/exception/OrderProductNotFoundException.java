package com.example.catalog.ordering.domain.exception;

public class OrderProductNotFoundException extends RuntimeException {
    public OrderProductNotFoundException(long id) {
        super("Product " + id + " not found");
    }
}
