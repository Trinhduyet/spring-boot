package com.example.catalog.category.application.service;

import com.example.catalog.category.application.input.CategoryInput;
import com.example.catalog.category.application.mapper.CategoryResultMapper;
import com.example.catalog.category.application.result.CategoryResult;
import com.example.catalog.category.domain.exception.CategoryConflictException;
import com.example.catalog.category.domain.exception.CategoryInUseException;
import com.example.catalog.category.domain.exception.CategoryNotFoundException;
import com.example.catalog.category.infrastructure.persistence.entity.CategoryEntity;
import com.example.catalog.category.infrastructure.persistence.entity.ProductCategoryEntity;
import com.example.catalog.category.infrastructure.persistence.entity.ProductCategoryId;
import com.example.catalog.category.infrastructure.persistence.repository.CategoryRepository;
import com.example.catalog.category.infrastructure.persistence.repository.ProductCategoryRepository;
import com.example.catalog.product.application.port.ProductCatalog;
import com.example.catalog.product.domain.exception.ProductNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private final ProductCategoryRepository links;
    private final ProductCatalog products;

    public CategoryService(
            CategoryRepository repository,
            ProductCategoryRepository links,
            ProductCatalog products) {
        this.repository = repository;
        this.links = links;
        this.products = products;
    }

    @Transactional(readOnly = true)
    public List<CategoryResult> list() {
        return repository.findAll().stream().map(CategoryResultMapper::toResult).toList();
    }

    @Transactional
    public CategoryResult create(CategoryInput request) {
        String slug = request.slug().trim().toLowerCase(java.util.Locale.ROOT);
        if (repository.existsBySlugIgnoreCase(slug)) throw new CategoryConflictException(slug);
        CategoryResult response;
        try {
            response =
                    CategoryResultMapper.toResult(
                            repository.saveAndFlush(
                                    new CategoryEntity(request.name(), slug, request.active())));
        } catch (DataIntegrityViolationException e) {
            throw new CategoryConflictException(slug);
        }
        return response;
    }

    @Transactional
    public CategoryResult update(long id, CategoryInput request) {
        CategoryEntity entity =
                repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        String slug = request.slug().trim().toLowerCase(java.util.Locale.ROOT);
        repository
                .findBySlugIgnoreCase(slug)
                .filter(other -> !other.getId().equals(id))
                .ifPresent(
                        other -> {
                            throw new CategoryConflictException(slug);
                        });
        entity.update(request.name(), slug, request.active());
        try {
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new CategoryConflictException(slug);
        }
        return CategoryResultMapper.toResult(entity);
    }

    @Transactional
    public void delete(long id) {
        CategoryEntity entity =
                repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        try {
            repository.delete(entity);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new CategoryInUseException(id);
        }
    }

    @Transactional(readOnly = true)
    public List<Long> productIds(long categoryId) {
        if (!repository.existsById(categoryId)) throw new CategoryNotFoundException(categoryId);
        return links.findByCategoryId(categoryId).stream()
                .map(ProductCategoryEntity::getProductId)
                .toList();
    }

    @Transactional
    public void assign(long categoryId, long productId) {
        CategoryEntity category =
                repository
                        .findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        if (!category.isActive())
            throw new IllegalArgumentException("Inactive category cannot be assigned");
        if (!products.exists(productId)) throw new ProductNotFoundException(productId);
        ProductCategoryId id = new ProductCategoryId(productId, categoryId);
        if (!links.existsById(id)) {
            links.save(new ProductCategoryEntity(productId, categoryId));
        }
    }

    @Transactional
    public void unassign(long categoryId, long productId) {
        if (!repository.existsById(categoryId)) throw new CategoryNotFoundException(categoryId);
        links.deleteByProductIdAndCategoryId(productId, categoryId);
    }
}
