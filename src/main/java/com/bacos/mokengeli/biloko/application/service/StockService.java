package com.bacos.mokengeli.biloko.application.service;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.port.StockPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockPort stockPort;

    public StockService(StockPort stockPort) {
        this.stockPort = stockPort;
    }

    public DomainStockMovement createStockMovement(DomainStockMovement stockMovement) {
        return stockPort.save(stockMovement);
    }

    public DomainStockMovement getStockMovementById(Long stockMovementId) {
        return stockPort.findById(stockMovementId)
                .orElseThrow(() -> new RuntimeException("Stock movement not found"));
    }

    public List<DomainStockMovement> getAllStockMovementsForTenant(Long tenantId) {
        return stockPort.findAllByTenantId(tenantId);
    }

    public void deleteStockMovement(Long stockMovementId) {
        stockPort.deleteById(stockMovementId);
    }
}
