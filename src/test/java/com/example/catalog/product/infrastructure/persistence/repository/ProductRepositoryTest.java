package com.example.catalog.product.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.catalog.category.infrastructure.persistence.entity.CategoryEntity;
import com.example.catalog.category.infrastructure.persistence.entity.ProductCategoryEntity;
import com.example.catalog.category.infrastructure.persistence.repository.CategoryRepository;
import com.example.catalog.category.infrastructure.persistence.repository.ProductCategoryRepository;
import com.example.catalog.product.infrastructure.persistence.entity.ProductEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {
    @Autowired ProductRepository products;
    @Autowired CategoryRepository categories;
    @Autowired ProductCategoryRepository productCategories;

    @Test
    void searchesByNameAndSkuWithPaging() {
        products.save(new ProductEntity("BOOK-1", "Java guide", new BigDecimal("25.00"), true));
        products.save(new ProductEntity("JAVA-2", "Java basics", new BigDecimal("30.00"), true));
        products.save(new ProductEntity("MUG-1", "Coffee mug", new BigDecimal("8.00"), true));

        var result = products.search("JAVA", PageRequest.of(0, 1, Sort.by("sku")));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getSku()).isEqualTo("BOOK-1");
    }

    @Test
    void filtersByCategoryWithoutLoadingCategoryPersistenceTypesInProductRepository() {
        ProductEntity assigned =
                products.save(
                        new ProductEntity(
                                "BOOK-CAT", "Catalog book", new BigDecimal("25.00"), true));
        products.save(new ProductEntity("MUG-CAT", "Catalog mug", new BigDecimal("8.00"), true));
        CategoryEntity category = categories.save(new CategoryEntity("Books", "books", true));
        productCategories.save(new ProductCategoryEntity(assigned.getId(), category.getId()));

        var result = products.search(null, category.getId(), PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent())
                .extracting(ProductEntity::getSku)
                .containsExactly("BOOK-CAT");
    }
}
