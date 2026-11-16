package com.example.catalog.inventory.application.port;

public interface InventoryOperations {
    void reserve(long productId, int quantity, String orderReference, String actor);

    void release(long productId, int quantity, String orderReference, String actor);
}
