package com.example.catalog.product.application.port;

import com.example.catalog.product.application.result.ProductSnapshot;

import java.util.Optional;

/** Small cross-feature contract used to validate product/category assignment. */
public interface ProductCatalog {
    boolean exists(long id);

    Optional<ProductSnapshot> find(long id);
}
