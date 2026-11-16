package com.example.catalog.category.infrastructure.persistence.repository;

import com.example.catalog.category.infrastructure.persistence.entity.ProductCategoryEntity;
import com.example.catalog.category.infrastructure.persistence.entity.ProductCategoryId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository
        extends JpaRepository<ProductCategoryEntity, ProductCategoryId> {
    List<ProductCategoryEntity> findByCategoryId(Long categoryId);

    List<ProductCategoryEntity> findByProductId(Long productId);

    void deleteByProductIdAndCategoryId(Long productId, Long categoryId);
}
