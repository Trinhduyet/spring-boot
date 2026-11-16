package com.example.catalog.product.infrastructure.persistence.repository;

import com.example.catalog.product.infrastructure.persistence.entity.ProductEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsBySkuIgnoreCase(String sku);

    Optional<ProductEntity> findBySkuIgnoreCase(String sku);

    @Query(
            """
            select p from ProductEntity p
            where :q is null
               or lower(p.sku) like lower(concat(concat('%', :q), '%'))
               or lower(p.name) like lower(concat(concat('%', :q), '%'))
            """)
    Page<ProductEntity> search(@Param("q") String query, Pageable pageable);

    @Query(
            value =
                    """
                    select p.* from products p
                    where (:q is null or lower(p.sku) like lower(concat(concat('%', :q), '%'))
                       or lower(p.name) like lower(concat(concat('%', :q), '%')))
                      and (:categoryId is null or exists (select 1 from product_categories pc
                           where pc.product_id = p.id and pc.category_id = :categoryId))
                    """,
            countQuery =
                    """
                    select count(*) from products p
                    where (:q is null or lower(p.sku) like lower(concat(concat('%', :q), '%'))
                       or lower(p.name) like lower(concat(concat('%', :q), '%')))
                      and (:categoryId is null or exists (select 1 from product_categories pc
                           where pc.product_id = p.id and pc.category_id = :categoryId))
                    """,
            nativeQuery = true)
    Page<ProductEntity> search(
            @Param("q") String query, @Param("categoryId") Long categoryId, Pageable pageable);
}
