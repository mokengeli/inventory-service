package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainStockAuditLog;

public interface StockAuditLogPort {
    DomainStockAuditLog save(DomainStockAuditLog stockAuditLog);
}
