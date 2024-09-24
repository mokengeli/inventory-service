package com.bacos.mokengeli.biloko.application.service;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.domain.MovementTypeEnum;
import com.bacos.mokengeli.biloko.application.port.ArticlePort;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
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
    private final UserAppService userAppService;

    @Autowired
    public ArticleService(ArticlePort articlePort, ProductPort productPort,
                          StockMovementPort stockMovementPort, UserAppService userAppService) {
        this.articlePort = articlePort;
        this.productPort = productPort;
        this.stockMovementPort = stockMovementPort;
        this.userAppService = userAppService;
    }

    @Transactional
    public DomainArticle addArticleToInventory(String productCode, int numberOfUnits) {
        String employeeNumber = this.userAppService.getConnectedEmployeeNumber();
        // Récupérer le produit via le port en filtrant par productId et tenantId
        DomainProduct product = productPort.findByCode(productCode);

        // Calculer le volume total à ajouter
        double totalVolumeToAdd = product.getVolume() * numberOfUnits;

        // Vérifier s'il existe déjà un article pour ce produit
        Optional<DomainArticle> existingArticleOpt = articlePort.findByProductId(product.getId());

        DomainArticle domainArticle;
        if (existingArticleOpt.isPresent()) {
            // Mettre à jour le volume total de l'article existant
            DomainArticle existingArticle = existingArticleOpt.get();
            double updatedTotalVolume = existingArticle.getTotalVolume() + totalVolumeToAdd;
            domainArticle = DomainArticle.builder()
                    .id(existingArticle.getId())
                    .productId(existingArticle.getProductId())
                    .totalVolume(updatedTotalVolume)
                    .createdAt(existingArticle.getCreatedAt())
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
                .movementType(MovementTypeEnum.ADD_ARTICLE.name())
                .employeeNumber(employeeNumber)
                .totalVolume(totalVolumeToAdd)
                .movementDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();


        stockMovementPort.createAndLogAudit(stockMovement);

        return domainArticle;
    }




}
