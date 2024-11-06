package com.bacos.mokengeli.biloko.infrastructure.mapper;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.infrastructure.model.StockMovement;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StockMapper {

    public DomainStockMovement toDomain(StockMovement stockMovement) {
        return DomainStockMovement.builder()
                .id(stockMovement.getId())
                .articleId(stockMovement.getArticle().getId())
                .movementType(stockMovement.getMovementType())
                .quantityMoved(stockMovement.getQuantityMoved())
                .movementDate(stockMovement.getMovementDate())
                .build();
    }

    public StockMovement toEntity(DomainStockMovement domainStockMovement) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setId(domainStockMovement.getId());
        //  stockMovement.setArticle(domainStockMovement.getArticleId());
        stockMovement.setQuantityMoved(domainStockMovement.getQuantityMoved());
        stockMovement.setMovementType(domainStockMovement.getMovementType());
        stockMovement.setMovementDate(domainStockMovement.getMovementDate());
        return stockMovement;
    }
}
