package com.example.catalog.ordering.domain.exception;

public class IdempotencyConflictException extends RuntimeException {
    public IdempotencyConflictException(String key) {
        super("Concurrent request with Idempotency-Key already exists: " + key);
    }
}
