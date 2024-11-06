package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;

import java.util.List;
import java.util.Optional;

public interface StockMovementPort {
    Optional<DomainStockMovement> createAndLogAudit(DomainStockMovement stockMovement);

    Optional<List<DomainStockMovement>>  createAndLogAuditList(List<DomainStockMovement> domainStockMovements);
}
