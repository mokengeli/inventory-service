package com.bacos.mokengeli.biloko.application.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainStockAuditLog {
    private Long id;
    private Long stockMovementId;
    private String employeeNumber;  // Récupéré via user-service
    private String actionType;
    private String description;
    private OffsetDateTime createdAt;
}

