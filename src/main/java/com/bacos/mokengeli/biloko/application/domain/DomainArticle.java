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
public class DomainArticle {
    private Long id;
    private Long productId;
    private double quantity;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

