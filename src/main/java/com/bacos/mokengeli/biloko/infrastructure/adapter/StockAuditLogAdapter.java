package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainStockAuditLog;
import com.bacos.mokengeli.biloko.application.port.StockAuditLogPort;
import com.bacos.mokengeli.biloko.infrastructure.model.StockAuditLog;
import com.bacos.mokengeli.biloko.infrastructure.repository.StockAuditLogRepository;
import com.bacos.mokengeli.biloko.infrastructure.mapper.StockAuditLogMapper;
import org.springframework.stereotype.Component;

@Component
public class StockAuditLogAdapter implements StockAuditLogPort {

    private final StockAuditLogRepository stockAuditLogRepository;

    public StockAuditLogAdapter(StockAuditLogRepository stockAuditLogRepository) {
        this.stockAuditLogRepository = stockAuditLogRepository;
    }

    @Override
    public DomainStockAuditLog save(DomainStockAuditLog auditLog) {
        StockAuditLog entity = StockAuditLogMapper.toEntity(auditLog);  // Mapping DomainStockAuditLog to StockAuditLog
        entity = stockAuditLogRepository.save(entity);
        return StockAuditLogMapper.toDomain(entity);  // Returning saved entity mapped to DomainStockAuditLog
    }
}
