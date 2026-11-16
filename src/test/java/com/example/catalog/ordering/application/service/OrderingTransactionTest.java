package com.example.catalog.ordering.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.catalog.audit.infrastructure.persistence.repository.AuditRecordRepository;
import com.example.catalog.category.infrastructure.persistence.repository.ProductCategoryRepository;
import com.example.catalog.inventory.application.result.InventoryResult;
import com.example.catalog.inventory.application.service.InventoryService;
import com.example.catalog.inventory.infrastructure.persistence.repository.InventoryMovementRepository;
import com.example.catalog.inventory.infrastructure.persistence.repository.InventoryRepository;
import com.example.catalog.ordering.application.input.CreateOrderInput;
import com.example.catalog.ordering.application.input.OrderLineInput;
import com.example.catalog.ordering.application.result.OrderResult;
import com.example.catalog.ordering.domain.exception.OrderProductNotFoundException;
import com.example.catalog.ordering.infrastructure.persistence.repository.OrderLineRepository;
import com.example.catalog.ordering.infrastructure.persistence.repository.OrderRepository;
import com.example.catalog.product.application.input.CreateProductInput;
import com.example.catalog.product.application.service.ProductService;
import com.example.catalog.product.infrastructure.persistence.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class OrderingTransactionTest {
    @Autowired ProductService products;
    @Autowired InventoryService inventory;
    @Autowired OrderService orders;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderLineRepository orderLines;
    @Autowired ProductRepository productRepository;
    @Autowired InventoryRepository inventoryRepository;
    @Autowired InventoryMovementRepository movementRepository;
    @Autowired AuditRecordRepository auditRepository;
    @Autowired ProductCategoryRepository categoryLinks;

    @BeforeEach
    void clearData() {
        orderLines.deleteAll();
        orderRepository.deleteAll();
        auditRepository.deleteAll();
        movementRepository.deleteAll();
        inventoryRepository.deleteAll();
        categoryLinks.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void repeatedKeyReturnsSameOrderAndReservesOnlyOnce() {
        long productId = createStockedProduct("ORDER-1", 4);
        var request = new CreateOrderInput(List.of(new OrderLineInput(productId, 2)));

        OrderResult first = orders.create("buyer-42", "checkout-0001", request);
        OrderResult replay = orders.create("buyer-42", "checkout-0001", request);

        assertThat(replay.id()).isEqualTo(first.id());
        assertThat(orderRepository.count()).isEqualTo(1);
        assertThat(inventory.get(productId).reserved()).isEqualTo(2);
    }

    @Test
    void failedSecondLineRollsBackOrderAndEarlierStockReservation() {
        long productId = createStockedProduct("ORDER-2", 3);
        var request =
                new CreateOrderInput(
                        List.of(new OrderLineInput(productId, 1), new OrderLineInput(999999L, 1)));

        assertThatThrownBy(() -> orders.create("buyer-42", "checkout-0002", request))
                .isInstanceOf(OrderProductNotFoundException.class);

        assertThat(orderRepository.count()).isZero();
        InventoryResult actual = inventory.get(productId);
        assertThat(actual.reserved()).isZero();
        assertThat(auditRepository.count())
                .isEqualTo(1); // only the opening-stock adjustment remains
    }

    @Test
    void cancellingOrderReleasesStockAndKeepsMovementHistory() {
        long productId = createStockedProduct("ORDER-CANCEL", 5);
        var request = new CreateOrderInput(List.of(new OrderLineInput(productId, 2)));
        OrderResult order = orders.create("buyer-42", "checkout-cancel", request);
        assertThat(inventory.get(productId).reserved()).isEqualTo(2);

        OrderResult cancelled = orders.cancel(order.id(), "buyer-42");

        assertThat(cancelled.status())
                .isEqualTo(com.example.catalog.ordering.domain.model.OrderStatus.CANCELLED);
        InventoryResult stock = inventory.get(productId);
        assertThat(stock.onHand()).isEqualTo(5);
        assertThat(stock.reserved()).isZero();
        assertThat(stock.available()).isEqualTo(5);
        assertThat(inventory.history(productId))
                .extracting(movement -> movement.type())
                .containsExactly("RELEASE", "RESERVE", "ADJUST");
    }

    private long createStockedProduct(String sku, int quantity) {
        long id =
                products.create(
                                new CreateProductInput(
                                        sku, "Transactional item", new BigDecimal("12.50"), true))
                        .id();
        inventory.adjust(id, quantity, "opening-stock", "integration-test");
        return id;
    }
}
