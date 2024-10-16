package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface ProductPort {

    Optional<DomainProduct> addProduct(DomainProduct product) throws ServiceException;

    Optional<DomainProduct> findById(Long id);

    Optional<DomainProduct> findByCode(String code);

    boolean isAllProductOfTenantCode(List<Long> productIds, String tenantCode);

    Optional<List<DomainProduct>> findByIds(List<Long> id);
}
