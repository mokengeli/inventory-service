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
    private String employeeNumber;  // Ajout du tenantId pour la traçabilité
    private String movementType;  // ENTREE ou SORTIE
    private double oldQuantity;
    private double quantityMoved;
    private double newQuantity;
    private String observation;
    private String unitOfMeasure;
    private LocalDateTime movementDate;
}

