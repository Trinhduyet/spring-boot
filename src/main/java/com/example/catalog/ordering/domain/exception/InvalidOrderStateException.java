package com.example.catalog.ordering.domain.exception;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(Long id) {
        super("Order " + id + " cannot transition from its current state");
    }
}
