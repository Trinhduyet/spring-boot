package com.example.catalog.product.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.catalog.product.application.input.CreateProductInput;
import com.example.catalog.product.application.result.ProductResult;
import com.example.catalog.product.domain.exception.ProductNotFoundException;
import com.example.catalog.product.domain.exception.SkuConflictException;
import com.example.catalog.product.infrastructure.persistence.entity.ProductEntity;
import com.example.catalog.product.infrastructure.persistence.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock ProductRepository repository;
    @InjectMocks ProductService service;

    @Test
    void createNormalizesSkuAndReturnsNewProduct() {
        when(repository.existsBySkuIgnoreCase("ABC-1")).thenReturn(false);
        when(repository.saveAndFlush(any(ProductEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ProductResult result =
                service.create(
                        new CreateProductInput("abc-1", "Keyboard", new BigDecimal("99.00"), true));

        assertThat(result.sku()).isEqualTo("ABC-1");
        assertThat(result.name()).isEqualTo("Keyboard");
        verify(repository).saveAndFlush(any(ProductEntity.class));
    }

    @Test
    void duplicateSkuReturnsConflict() {
        when(repository.existsBySkuIgnoreCase("ABC-1")).thenReturn(true);

        assertThatThrownBy(
                        () ->
                                service.create(
                                        new CreateProductInput(
                                                "abc-1",
                                                "Keyboard",
                                                new BigDecimal("99.00"),
                                                true)))
                .isInstanceOf(SkuConflictException.class);
    }

    @Test
    void missingProductReturnsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(99L)).isInstanceOf(ProductNotFoundException.class);
    }
}
