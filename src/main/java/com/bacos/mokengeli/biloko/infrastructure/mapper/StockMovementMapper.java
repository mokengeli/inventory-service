package com.bacos.mokengeli.biloko.infrastructure.mapper;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.infrastructure.model.StockMovement;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StockMovementMapper {

    public static DomainStockMovement toDomain(StockMovement stockMovement) {
        return DomainStockMovement.builder()
                .id(stockMovement.getId())
                .articleId(stockMovement.getArticle().getId())
                .movementType(stockMovement.getMovementType())
                .quantityMoved(stockMovement.getQuantityMoved())
                .movementDate(stockMovement.getMovementDate())
                .unitOfMeasure(stockMovement.getUnitOfMeasure())
                .observation(stockMovement.getObservation())
                .build();
    }

    public static StockMovement toEntity(DomainStockMovement domainStockMovement) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setId(domainStockMovement.getId());
        // On récupère uniquement l'id du produit et de l'article
        stockMovement.setMovementType(domainStockMovement.getMovementType());
        stockMovement.setEmployeeNumber(domainStockMovement.getEmployeeNumber());
        stockMovement.setOldQuantity(domainStockMovement.getOldQuantity());
        stockMovement.setQuantityMoved(domainStockMovement.getQuantityMoved());
        stockMovement.setNewQuantity(domainStockMovement.getNewQuantity());
        stockMovement.setMovementDate(domainStockMovement.getMovementDate());
        stockMovement.setUnitOfMeasure(domainStockMovement.getUnitOfMeasure());
        stockMovement.setObservation(domainStockMovement.getObservation());
        return stockMovement;
    }
}
