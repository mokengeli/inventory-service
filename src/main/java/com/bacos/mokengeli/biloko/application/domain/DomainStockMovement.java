package com.bacos.mokengeli.biloko.application.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainStockMovement {
    private Long id;
    private Long articleId;
    private Long tenantId;  // Ajout du tenantId pour la traçabilité
    private String movementType;  // ENTREE ou SORTIE
    private double quantityMoved;
    private LocalDateTime movementDate;
    private LocalDateTime createdAt;
}

