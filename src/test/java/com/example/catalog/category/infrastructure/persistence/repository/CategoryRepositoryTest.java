package com.example.catalog.category.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.catalog.category.infrastructure.persistence.entity.CategoryEntity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {
    @Autowired CategoryRepository categories;

    @Test
    void findsSlugIgnoringCase() {
        categories.saveAndFlush(new CategoryEntity("Programming", "programming", true));

        assertThat(categories.existsBySlugIgnoreCase("PROGRAMMING")).isTrue();
        assertThat(categories.findBySlugIgnoreCase("Programming"))
                .get()
                .extracting(CategoryEntity::getName)
                .isEqualTo("Programming");
    }
}
