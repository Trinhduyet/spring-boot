package com.example.catalog.inventory.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.catalog.audit.application.port.AuditRecorder;
import com.example.catalog.inventory.domain.exception.InsufficientStockException;
import com.example.catalog.inventory.infrastructure.persistence.adapter.InventoryStockWriter;
import com.example.catalog.inventory.infrastructure.persistence.entity.InventoryEntity;
import com.example.catalog.inventory.infrastructure.persistence.repository.InventoryMovementRepository;
import com.example.catalog.inventory.infrastructure.persistence.repository.InventoryRepository;
import com.example.catalog.product.application.port.ProductCatalog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
    @Mock InventoryRepository inventory;
    @Mock InventoryMovementRepository movements;
    @Mock ProductCatalog products;
    @Mock InventoryStockWriter stockWriter;
    @Mock AuditRecorder audit;
    @InjectMocks InventoryService service;

    @Test
    void reservationCannotExceedAvailableStock() {
        when(inventory.lockByProductId(7L)).thenReturn(Optional.of(new InventoryEntity(7L, 3)));

        assertThatThrownBy(() -> service.reserve(7L, 4, "order:abc12345", "buyer"))
                .isInstanceOf(InsufficientStockException.class);

        verifyNoInteractions(movements, stockWriter, audit);
    }

    @Test
    void releaseCannotExceedReservedStock() {
        when(inventory.lockByProductId(7L)).thenReturn(Optional.of(new InventoryEntity(7L, 3)));

        assertThatThrownBy(() -> service.release(7L, 1, "order:abc12345", "buyer"))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(movements, stockWriter, audit);
    }
}
