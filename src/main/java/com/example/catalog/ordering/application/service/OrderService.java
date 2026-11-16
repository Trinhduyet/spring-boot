package com.example.catalog.ordering.application.service;

import com.example.catalog.audit.application.port.AuditRecorder;
import com.example.catalog.inventory.application.port.InventoryOperations;
import com.example.catalog.ordering.application.input.CreateOrderInput;
import com.example.catalog.ordering.application.input.OrderLineInput;
import com.example.catalog.ordering.application.mapper.OrderResultMapper;
import com.example.catalog.ordering.application.result.OrderResult;
import com.example.catalog.ordering.domain.exception.IdempotencyConflictException;
import com.example.catalog.ordering.domain.exception.OrderNotFoundException;
import com.example.catalog.ordering.domain.exception.OrderProductNotFoundException;
import com.example.catalog.ordering.infrastructure.persistence.entity.OrderEntity;
import com.example.catalog.ordering.infrastructure.persistence.entity.OrderLineEntity;
import com.example.catalog.ordering.infrastructure.persistence.repository.OrderLineRepository;
import com.example.catalog.ordering.infrastructure.persistence.repository.OrderRepository;
import com.example.catalog.product.application.port.ProductCatalog;
import com.example.catalog.product.application.result.ProductSnapshot;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final OrderLineRepository lines;
    private final ProductCatalog products;
    private final InventoryOperations inventory;
    private final AuditRecorder audit;

    public OrderService(
            OrderRepository orders,
            OrderLineRepository lines,
            ProductCatalog products,
            InventoryOperations inventory,
            AuditRecorder audit) {
        this.orders = orders;
        this.lines = lines;
        this.products = products;
        this.inventory = inventory;
        this.audit = audit;
    }

    @Transactional
    public OrderResult create(String actor, String key, CreateOrderInput request) {
        if (actor == null || actor.isBlank())
            throw new IllegalArgumentException("Authenticated actor is required");
        if (key == null || !key.matches("[A-Za-z0-9._:-]{8,128}"))
            throw new IllegalArgumentException(
                    "A valid Idempotency-Key (8-128 safe characters) is required");
        var existing = orders.findByActorIdAndIdempotencyKey(actor, key);
        if (existing.isPresent())
            return OrderResultMapper.toResult(
                    existing.get(), lines.findByOrderId(existing.get().getId()));
        OrderEntity order = orders.saveAndFlush(new OrderEntity(actor, key));
        java.util.Map<Long, Integer> quantities = new java.util.TreeMap<>();
        for (OrderLineInput requested : request.lines())
            quantities.merge(requested.productId(), requested.quantity(), Math::addExact);
        List<OrderLineEntity> saved = new ArrayList<>();
        for (var item : quantities.entrySet()) {
            ProductSnapshot product =
                    products.find(item.getKey())
                            .orElseThrow(() -> new OrderProductNotFoundException(item.getKey()));
            if (!product.active())
                throw new IllegalArgumentException("Product is inactive: " + product.id());
            inventory.reserve(product.id(), item.getValue(), "order:" + key, actor);
            saved.add(
                    lines.save(
                            new OrderLineEntity(
                                    order,
                                    product.id(),
                                    product.sku(),
                                    product.name(),
                                    item.getValue(),
                                    product.price())));
        }
        try {
            lines.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new IdempotencyConflictException(key);
        }
        audit.record(actor, "order.create", "order", Long.toString(order.getId()));
        return OrderResultMapper.toResult(order, saved);
    }

    @Transactional(readOnly = true)
    public OrderResult get(long id, String actor) {
        OrderEntity order =
                orders.findByIdAndActorId(id, actor)
                        .orElseThrow(() -> new OrderNotFoundException(id));
        return OrderResultMapper.toResult(order, lines.findByOrderId(id));
    }

    @Transactional
    public OrderResult cancel(long id, String actor) {
        OrderEntity order =
                orders.findByIdAndActorId(id, actor)
                        .orElseThrow(() -> new OrderNotFoundException(id));
        List<OrderLineEntity> orderLines = lines.findByOrderId(id);
        order.cancel();
        for (OrderLineEntity line : orderLines)
            inventory.release(
                    line.getProductId(), line.getQuantity(), "order:" + order.getId(), actor);
        audit.record(actor, "order.cancel", "order", Long.toString(order.getId()));
        return OrderResultMapper.toResult(order, orderLines);
    }
}
