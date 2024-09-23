package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;

public interface StockMovementPort {
    DomainStockMovement save(DomainStockMovement stockMovement);
}
