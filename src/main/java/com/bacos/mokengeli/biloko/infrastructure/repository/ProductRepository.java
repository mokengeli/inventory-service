package com.bacos.mokengeli.biloko.infrastructure.repository;

import com.bacos.mokengeli.biloko.infrastructure.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);

    @Query("SELECT COUNT(p) = :size FROM Product p WHERE p.id IN :productIds AND p.tenantCode = :tenantCode")
    boolean isAllProductOfTenantCode(@Param("productIds") List<Long> productIds,
                                     @Param("tenantCode") String tenantCode, @Param("size") int size);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    Optional<List<Product>> findByIds(@Param("productIds") List<Long> productIds);

    @Query("SELECT p FROM Product p WHERE p.tenantCode = :tenantCode")
    Page<Product> getAllProductOfTenantCode(@Param("tenantCode") String tenantCode, Pageable pageable);

    Page<Product> findByTenantCodeAndNameContainingIgnoreCase(
            String tenantCode,
            String name,
            Pageable pageable
    );
}
