package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductPort {

    Optional<DomainProduct> addProduct(DomainProduct product) throws ServiceException;

    Optional<DomainProduct> findById(Long id);

    Optional<DomainProduct> findByCode(String code);

    boolean isAllProductOfTenantCode(List<Long> productIds, String tenantCode);

    Optional<List<DomainProduct>> findByIds(List<Long> id);

    Optional<List<DomainProduct>> getAllProducts();

    Page<DomainProduct> getAllProductsByTenant(String tenantCode, int page, int size, String search);

    Page<String> getAllUnitOfMeasurement(
            int    page,
            int    size,
            String search
    );}
