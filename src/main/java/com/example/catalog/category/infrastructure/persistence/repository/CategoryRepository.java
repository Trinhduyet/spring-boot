package com.example.catalog.category.infrastructure.persistence.repository;

import com.example.catalog.category.infrastructure.persistence.entity.CategoryEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsBySlugIgnoreCase(String slug);

    Optional<CategoryEntity> findBySlugIgnoreCase(String slug);
}
