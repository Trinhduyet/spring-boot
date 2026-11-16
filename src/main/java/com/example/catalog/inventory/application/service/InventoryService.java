package com.example.catalog.inventory.application.service;

import com.example.catalog.audit.application.port.AuditRecorder;
import com.example.catalog.inventory.application.mapper.InventoryResultMapper;
import com.example.catalog.inventory.application.port.InventoryOperations;
import com.example.catalog.inventory.application.result.InventoryMovementResult;
import com.example.catalog.inventory.application.result.InventoryResult;
import com.example.catalog.inventory.domain.exception.InsufficientStockException;
import com.example.catalog.inventory.domain.exception.InventoryNotFoundException;
import com.example.catalog.inventory.infrastructure.persistence.adapter.InventoryStockWriter;
import com.example.catalog.inventory.infrastructure.persistence.entity.InventoryEntity;
import com.example.catalog.inventory.infrastructure.persistence.entity.InventoryMovementEntity;
import com.example.catalog.inventory.infrastructure.persistence.repository.InventoryMovementRepository;
import com.example.catalog.inventory.infrastructure.persistence.repository.InventoryRepository;
import com.example.catalog.product.application.port.ProductCatalog;
import com.example.catalog.product.domain.exception.ProductNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService implements InventoryOperations {
    private final InventoryRepository inventory;
    private final InventoryMovementRepository movements;
    private final ProductCatalog products;
    private final InventoryStockWriter stockWriter;
    private final AuditRecorder audit;

    public InventoryService(
            InventoryRepository inventory,
            InventoryMovementRepository movements,
            ProductCatalog products,
            InventoryStockWriter stockWriter,
            AuditRecorder audit) {
        this.inventory = inventory;
        this.movements = movements;
        this.products = products;
        this.stockWriter = stockWriter;
        this.audit = audit;
    }

    @Transactional(readOnly = true)
    public InventoryResult get(long productId) {
        InventoryEntity entity =
                inventory
                        .findById(productId)
                        .orElseThrow(() -> new InventoryNotFoundException(productId));
        return InventoryResultMapper.toResult(entity);
    }

    @Transactional
    public InventoryResult adjust(long productId, int delta, String reference, String actor) {
        if (delta == 0) {
            throw new IllegalArgumentException("Adjustment quantity must not be zero");
        }
        if (!products.exists(productId)) {
            throw new ProductNotFoundException(productId);
        }
        InventoryEntity row =
                inventory
                        .lockByProductId(productId)
                        .orElseGet(() -> inventory.saveAndFlush(new InventoryEntity(productId, 0)));
        int newOnHand = row.getOnHand() + delta;
        if (newOnHand < row.getReserved()) {
            throw new InsufficientStockException(productId);
        }
        stockWriter.setOnHand(productId, newOnHand);
        movements.save(
                new InventoryMovementEntity(productId, "ADJUST", delta, safeReference(reference)));
        audit.record(safeActor(actor), "inventory.adjust", "product", Long.toString(productId));
        return getAfterUpdate(productId);
    }

    @Transactional
    @Override
    public void reserve(long productId, int quantity, String orderReference, String actor) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        InventoryEntity row =
                inventory
                        .lockByProductId(productId)
                        .orElseThrow(() -> new InsufficientStockException(productId));
        if (row.getAvailable() < quantity) {
            throw new InsufficientStockException(productId);
        }
        stockWriter.reserve(productId, quantity);
        movements.save(
                new InventoryMovementEntity(
                        productId, "RESERVE", quantity, safeReference(orderReference)));
        audit.record(safeActor(actor), "inventory.reserve", "product", Long.toString(productId));
    }

    @Transactional
    @Override
    public void release(long productId, int quantity, String orderReference, String actor) {
        InventoryEntity row =
                inventory
                        .lockByProductId(productId)
                        .orElseThrow(() -> new InventoryNotFoundException(productId));
        if (quantity < 1 || row.getReserved() < quantity) {
            throw new IllegalArgumentException("Release exceeds reserved quantity");
        }
        stockWriter.release(productId, quantity);
        movements.save(
                new InventoryMovementEntity(
                        productId, "RELEASE", quantity, safeReference(orderReference)));
        audit.record(safeActor(actor), "inventory.release", "product", Long.toString(productId));
    }

    @Transactional(readOnly = true)
    public List<InventoryMovementResult> history(long productId) {
        if (!inventory.existsById(productId)) {
            throw new InventoryNotFoundException(productId);
        }
        return movements.findTop100ByProductIdOrderByIdDesc(productId).stream()
                .map(InventoryResultMapper::toMovementResult)
                .toList();
    }

    private InventoryResult getAfterUpdate(long id) {
        stockWriter.flushAndClear();
        return get(id);
    }

    private String safeReference(String value) {
        String result = value == null || value.isBlank() ? "manual" : value.trim();
        if (result.length() > 200)
            throw new IllegalArgumentException("Reference must be at most 200 characters");
        return result;
    }

    private String safeActor(String actor) {
        String value = actor == null || actor.isBlank() ? "system" : actor.trim();
        if (value.length() > 200)
            throw new IllegalArgumentException("Actor identifier must be at most 200 characters");
        return value;
    }
}
