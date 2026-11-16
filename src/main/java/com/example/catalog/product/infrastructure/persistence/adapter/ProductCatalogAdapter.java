package com.example.catalog.product.infrastructure.persistence.adapter;

import com.example.catalog.product.application.port.ProductCatalog;
import com.example.catalog.product.application.result.ProductSnapshot;
import com.example.catalog.product.infrastructure.persistence.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductCatalogAdapter implements ProductCatalog {
    private final ProductRepository repository;

    public ProductCatalogAdapter(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductSnapshot> find(long id) {
        return repository
                .findById(id)
                .map(
                        p ->
                                new ProductSnapshot(
                                        p.getId(),
                                        p.getSku(),
                                        p.getName(),
                                        p.getPrice(),
                                        p.isActive()));
    }
}
