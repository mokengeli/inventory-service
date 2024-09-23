package com.bacos.mokengeli.biloko.application.service;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.domain.DomainStockAuditLog;
import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.port.ArticlePort;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
import com.bacos.mokengeli.biloko.application.port.StockAuditLogPort;
import com.bacos.mokengeli.biloko.application.port.StockMovementPort;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticlePort articlePort;
    private final ProductPort productPort;
    private final StockMovementPort stockMovementPort;
    private final StockAuditLogPort stockAuditLogPort;

    @Autowired
    public ArticleService(ArticlePort articlePort, ProductPort productPort,
                          StockMovementPort stockMovementPort, StockAuditLogPort stockAuditLogPort) {
        this.articlePort = articlePort;
        this.productPort = productPort;
        this.stockMovementPort = stockMovementPort;
        this.stockAuditLogPort = stockAuditLogPort;
    }

    @Transactional
    public DomainArticle addArticleToInventory(Long productId, int numberOfUnits) {
        String employeeNumber ="";
        // Récupérer le produit via le port en filtrant par productId et tenantId
        DomainProduct product = productPort.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found for with id: " + productId));

        // Calculer le volume total à ajouter
        double totalVolumeToAdd = product.getVolume() * numberOfUnits;

        // Vérifier s'il existe déjà un article pour ce produit
        Optional<DomainArticle> existingArticleOpt = articlePort.findByProductId(productId);

        DomainArticle domainArticle;
        if (existingArticleOpt.isPresent()) {
            // Mettre à jour le volume total de l'article existant
            DomainArticle existingArticle = existingArticleOpt.get();
            double updatedTotalVolume = existingArticle.getTotalVolume() + totalVolumeToAdd;
            domainArticle = DomainArticle.builder()
                    .id(existingArticle.getId())
                    .productId(existingArticle.getProductId())
                    .totalVolume(updatedTotalVolume)
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            // Créer un nouvel article
            domainArticle = DomainArticle.builder()
                    .productId(product.getId())
                    .totalVolume(totalVolumeToAdd)
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        // Sauvegarder l'article via le port
        domainArticle = articlePort.save(domainArticle);

        // Créer un mouvement de stock de type ENTREE
        DomainStockMovement stockMovement = DomainStockMovement.builder()
                .articleId(domainArticle.getId())
                .movementType("ENTREE")
                .quantityMoved(totalVolumeToAdd)
                .movementDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        stockMovementPort.save(stockMovement);

        // Créer un log d'audit pour tracer l'ajout de stock
        DomainStockAuditLog auditLog = DomainStockAuditLog.builder()
                .stockMovementId(stockMovement.getId())
                .employeeNumber(employeeNumber)
                .actionType("CREATE")
                .description("Ajout de " + numberOfUnits + " unités pour le produit ID " + productId)
                .createdAt(LocalDateTime.now())
                .build();
        stockAuditLogPort.save(auditLog);

        return domainArticle;
    }


    public DomainArticle getArticleById(Long articleId) {
        return articlePort.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public List<DomainArticle> getAllArticlesByProductId(Long productId) {
        return articlePort.findAllByProductId(productId);
    }

    public void deleteArticle(Long articleId) {
        articlePort.deleteById(articleId);
    }


}
