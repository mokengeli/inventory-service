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
public class DomainProduct {

    private Long id;
    private String tenantCode;
    private String code;
    private String name;
    private DomainCategory category;
    private String description;
    private String unitOfMeasure;
    private double volume;  // Volume unitaire du produit
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private DomainArticle article;
}
