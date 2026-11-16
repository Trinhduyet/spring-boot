package com.example.catalog.inventory.domain.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(long id) {
        super("Inventory for product " + id + " not found");
    }
}
