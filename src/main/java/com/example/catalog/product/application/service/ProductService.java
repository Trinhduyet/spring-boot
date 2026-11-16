package com.example.catalog.product.application.service;

import com.example.catalog.product.application.input.CreateProductInput;
import com.example.catalog.product.application.input.UpdateProductInput;
import com.example.catalog.product.application.mapper.ProductResultMapper;
import com.example.catalog.product.application.result.ProductResult;
import com.example.catalog.product.domain.exception.ProductNotFoundException;
import com.example.catalog.product.domain.exception.SkuConflictException;
import com.example.catalog.product.infrastructure.persistence.entity.ProductEntity;
import com.example.catalog.product.infrastructure.persistence.repository.ProductRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ProductResult> list(String search, Pageable pageable) {
        return list(search, null, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProductResult> list(String search, Long categoryId, Pageable pageable) {
        String q = search == null || search.isBlank() ? null : search.trim();
        return repository.search(q, categoryId, pageable).map(ProductResultMapper::toResult);
    }

    @Transactional(readOnly = true)
    public ProductResult get(Long id) {
        return ProductResultMapper.toResult(find(id));
    }

    @Transactional
    public ProductResult create(CreateProductInput input) {
        String sku = input.sku().toUpperCase(java.util.Locale.ROOT);
        if (repository.existsBySkuIgnoreCase(sku)) {
            throw new SkuConflictException(sku);
        }
        ProductEntity entity = new ProductEntity(sku, input.name(), input.price(), input.active());
        try {
            return ProductResultMapper.toResult(repository.saveAndFlush(entity));
        } catch (DataIntegrityViolationException ex) {
            // Unique constraint is the final guard against concurrent create requests.
            if (isSkuConstraint(ex)) throw new SkuConflictException(sku);
            throw ex;
        }
    }

    @Transactional
    public ProductResult update(Long id, UpdateProductInput input) {
        ProductEntity entity = find(id);
        entity.update(input.name(), input.price(), input.active());
        repository.flush();
        return ProductResultMapper.toResult(entity);
    }

    @Transactional
    public void delete(Long id) {
        ProductEntity entity = find(id);
        repository.delete(entity);
    }

    private ProductEntity find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    private boolean isSkuConstraint(DataIntegrityViolationException exception) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause instanceof org.hibernate.exception.ConstraintViolationException violation) {
                return "ux_products_sku_lower".equalsIgnoreCase(violation.getConstraintName());
            }
            cause = cause.getCause();
        }
        return false;
    }
}
