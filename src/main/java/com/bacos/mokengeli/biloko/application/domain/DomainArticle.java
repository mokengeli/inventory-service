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
public class DomainArticle {
    private Long id;
    private Long productId;
    private Long tenantId;  // Ajout du tenantId pour plus de clarté
    private double totalVolume;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

