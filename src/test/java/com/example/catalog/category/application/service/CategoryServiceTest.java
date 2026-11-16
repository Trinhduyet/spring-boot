package com.example.catalog.category.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.catalog.category.application.input.CategoryInput;
import com.example.catalog.category.application.result.CategoryResult;
import com.example.catalog.category.domain.exception.CategoryConflictException;
import com.example.catalog.category.infrastructure.persistence.entity.CategoryEntity;
import com.example.catalog.category.infrastructure.persistence.repository.CategoryRepository;
import com.example.catalog.category.infrastructure.persistence.repository.ProductCategoryRepository;
import com.example.catalog.product.application.port.ProductCatalog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock CategoryRepository categories;
    @Mock ProductCategoryRepository links;
    @Mock ProductCatalog products;
    @InjectMocks CategoryService service;

    @Test
    void createNormalizesSlugBeforePersistence() {
        when(categories.existsBySlugIgnoreCase("desk-accessories")).thenReturn(false);
        when(categories.saveAndFlush(any(CategoryEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResult response =
                service.create(new CategoryInput("Desk Accessories", " Desk-Accessories ", true));

        ArgumentCaptor<CategoryEntity> saved = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(categories).saveAndFlush(saved.capture());
        assertThat(saved.getValue().getSlug()).isEqualTo("desk-accessories");
        assertThat(response.name()).isEqualTo("Desk Accessories");
    }

    @Test
    void duplicateSlugIsRejectedBeforeInsert() {
        when(categories.existsBySlugIgnoreCase("tools")).thenReturn(true);

        assertThatThrownBy(() -> service.create(new CategoryInput("Tools", "TOOLS", true)))
                .isInstanceOf(CategoryConflictException.class);
        verify(categories, never()).saveAndFlush(any());
    }
}
