package com.example.catalog.inventory.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

@Repository
public class InventoryStockWriter {
    private final EntityManager entityManager;

    public InventoryStockWriter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setOnHand(long productId, int newOnHand) {
        entityManager
                .createNativeQuery(
                        "update inventory set on_hand = :qty, version = version + 1, "
                                + "updated_at = CURRENT_TIMESTAMP where product_id = :id")
                .setParameter("qty", newOnHand)
                .setParameter("id", productId)
                .executeUpdate();
    }

    public void reserve(long productId, int quantity) {
        entityManager
                .createNativeQuery(
                        "update inventory set reserved = reserved + :qty, version = version + 1, "
                                + "updated_at = CURRENT_TIMESTAMP where product_id = :id")
                .setParameter("qty", quantity)
                .setParameter("id", productId)
                .executeUpdate();
    }

    public void release(long productId, int quantity) {
        entityManager
                .createNativeQuery(
                        "update inventory set reserved = reserved - :qty, version = version + 1, "
                                + "updated_at = CURRENT_TIMESTAMP where product_id = :id")
                .setParameter("qty", quantity)
                .setParameter("id", productId)
                .executeUpdate();
    }

    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
