package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;

import java.util.List;
import java.util.Optional;

public interface ProductPort {

    DomainProduct save(DomainProduct product);

    Optional<DomainProduct> findById(Long id);

    void deleteById(Long id);

    List<DomainProduct> findAllByTenantCode(String tenantCode);

    Optional<DomainProduct> getProductByIdAndTenantId(Long productId, Long tenantId);

}
