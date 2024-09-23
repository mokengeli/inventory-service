package com.bacos.mokengeli.biloko.infrastructure.repository;

import com.bacos.mokengeli.biloko.infrastructure.model.StockAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAuditLogRepository extends JpaRepository<StockAuditLog, Long> {
    // Méthodes personnalisées si nécessaire
}
