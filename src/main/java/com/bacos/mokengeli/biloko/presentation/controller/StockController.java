package com.bacos.mokengeli.biloko.presentation.controller;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Enregistrer un nouveau mouvement de stock (entrée ou sortie)
    @PostMapping
    public ResponseEntity<DomainStockMovement> createStockMovement(@RequestBody DomainStockMovement stockMovement) {
        DomainStockMovement createdStockMovement = stockService.createStockMovement(stockMovement);
        return ResponseEntity.ok(createdStockMovement);
    }

    // Récupérer un mouvement de stock par ID
    @GetMapping("/{stockMovementId}")
    public ResponseEntity<DomainStockMovement> getStockMovementById(@PathVariable Long stockMovementId) {
        DomainStockMovement stockMovement = stockService.getStockMovementById(stockMovementId);
        return ResponseEntity.ok(stockMovement);
    }

    // Lister tous les mouvements de stock pour un tenant
    @GetMapping
    public ResponseEntity<List<DomainStockMovement>> getAllStockMovements(@RequestParam Long tenantId) {
        List<DomainStockMovement> stockMovements = stockService.getAllStockMovementsForTenant(tenantId);
        return ResponseEntity.ok(stockMovements);
    }

    // Supprimer un mouvement de stock
    @DeleteMapping("/{stockMovementId}")
    public ResponseEntity<Void> deleteStockMovement(@PathVariable Long stockMovementId) {
        stockService.deleteStockMovement(stockMovementId);
        return ResponseEntity.noContent().build();
    }
}
