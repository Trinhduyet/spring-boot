package com.example.catalog.ordering.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.catalog.audit.application.port.AuditRecorder;
import com.example.catalog.inventory.application.port.InventoryOperations;
import com.example.catalog.ordering.application.input.CreateOrderInput;
import com.example.catalog.ordering.application.result.OrderResult;
import com.example.catalog.ordering.domain.model.OrderStatus;
import com.example.catalog.ordering.infrastructure.persistence.entity.OrderEntity;
import com.example.catalog.ordering.infrastructure.persistence.repository.OrderLineRepository;
import com.example.catalog.ordering.infrastructure.persistence.repository.OrderRepository;
import com.example.catalog.product.application.port.ProductCatalog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock OrderRepository orders;
    @Mock OrderLineRepository lines;
    @Mock ProductCatalog products;
    @Mock InventoryOperations inventory;
    @Mock AuditRecorder audit;
    @InjectMocks OrderService service;

    @Test
    void idempotentReplayReturnsOriginalOrderWithoutReservingAgain() {
        OrderEntity original = org.mockito.Mockito.mock(OrderEntity.class);
        when(original.getId()).thenReturn(42L);
        when(original.getStatus()).thenReturn(OrderStatus.RESERVED);
        when(original.getCreatedAt()).thenReturn(Instant.parse("2026-01-01T00:00:00Z"));
        when(original.getUpdatedAt()).thenReturn(Instant.parse("2026-01-01T00:00:00Z"));
        when(orders.findByActorIdAndIdempotencyKey("buyer", "checkout-0001"))
                .thenReturn(Optional.of(original));
        when(lines.findByOrderId(42L)).thenReturn(List.of());

        OrderResult replay =
                service.create("buyer", "checkout-0001", new CreateOrderInput(List.of()));

        assertThat(replay.id()).isEqualTo(42L);
        assertThat(replay.total()).isZero();
        verify(inventory, never())
                .reserve(
                        org.mockito.ArgumentMatchers.anyLong(),
                        org.mockito.ArgumentMatchers.anyInt(),
                        org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers.anyString());
    }
}
