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
public class DomainProduct {

    private Long id;
    private String tenantCode;  // Ajout du tenantId pour la gestion multi-tenant
    private String name;
    private String category;
    private String description;
    private String unitOfMeasure;
    private double volume;  // Volume unitaire du produit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
