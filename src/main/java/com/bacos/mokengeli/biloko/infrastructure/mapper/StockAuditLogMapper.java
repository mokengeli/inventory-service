package com.bacos.mokengeli.biloko.infrastructure.mapper;

import com.bacos.mokengeli.biloko.application.domain.DomainStockAuditLog;
import com.bacos.mokengeli.biloko.infrastructure.model.StockAuditLog;
import com.bacos.mokengeli.biloko.infrastructure.model.StockMovement;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StockAuditLogMapper {

    public static DomainStockAuditLog toDomain(StockAuditLog stockAuditLog) {
        return DomainStockAuditLog.builder()
                .id(stockAuditLog.getId())
                .stockMovementId(stockAuditLog.getStockMovement().getId())
                .employeeNumber(stockAuditLog.getEmployeeNumber())
                .actionType(stockAuditLog.getActionType())
                .description(stockAuditLog.getDescription())
                .createdAt(stockAuditLog.getCreatedAt())
                .build();
    }

    public static StockAuditLog toEntity(DomainStockAuditLog domainAuditLog) {
        StockAuditLog stockAuditLog = new StockAuditLog();
        stockAuditLog.setId(domainAuditLog.getId());
        // On récupère uniquement l'id du mouvement
        //stockAuditLog.setStockMovement(new StockMovement(domainAuditLog.getStockMovementId()));
        stockAuditLog.setEmployeeNumber(domainAuditLog.getEmployeeNumber());
        stockAuditLog.setActionType(domainAuditLog.getActionType());
        stockAuditLog.setDescription(domainAuditLog.getDescription());
        stockAuditLog.setCreatedAt(domainAuditLog.getCreatedAt());
        return stockAuditLog;
    }
}
