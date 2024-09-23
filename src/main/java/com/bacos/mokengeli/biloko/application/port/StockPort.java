package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;

import java.util.List;
import java.util.Optional;

public interface StockPort {

    DomainStockMovement save(DomainStockMovement stockMovement);

    Optional<DomainStockMovement> findById(Long id);

    void deleteById(Long id);

    List<DomainStockMovement> findAllByTenantId(Long tenantId);
}
