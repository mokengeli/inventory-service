package com.bacos.mokengeli.biloko.application.service;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.domain.MovementTypeEnum;
import com.bacos.mokengeli.biloko.application.domain.model.ConnectedUser;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.port.ArticlePort;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
import com.bacos.mokengeli.biloko.application.port.StockMovementPort;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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
    public DomainArticle addArticleToInventory(String productCode, int numberOfUnits) throws ServiceException {

        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String employeeNumber = connectedUser.getEmployeeNumber();
        // Récupérer le produit via le port en filtrant par productId et tenantId
        Optional<DomainProduct> optProduct = productPort.findByCode(productCode);
        if (optProduct.isEmpty()) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}] try to add article with non existing product code [{}]", errorId, employeeNumber, productCode);
            throw new ServiceException(errorId, "The product does not exist");
        }

        DomainProduct product = optProduct.get();
        if (!this.userAppService.isAdminUser()
                && !product.getTenantCode().equals(connectedUser.getTenantCode())) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}] of tenant [{}] try to add article of tenant [{}] to the stock", errorId, employeeNumber, connectedUser.getTenantCode(), product.getTenantCode());
            throw new ServiceException(errorId, "You can't add item owning by another partener");
        }


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
        Optional<DomainArticle> optionalDomainArticle = articlePort.save(domainArticle);
        if (optionalDomainArticle.isEmpty()) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}]. Something went wrong while adding article of product code [{}] ", errorId, employeeNumber, productCode);
            throw new ServiceException(errorId, "Something went wrong while adding the product.");
        }

        domainArticle = optionalDomainArticle.get();
        // Créer un mouvement de stock de type ENTREE
        DomainStockMovement stockMovement = DomainStockMovement.builder()
                .articleId(domainArticle.getId())
                .movementType(MovementTypeEnum.ADD_ARTICLE.name())
                .employeeNumber(employeeNumber)
                .totalVolume(totalVolumeToAdd)
                .movementDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();


        Optional<DomainStockMovement> logAudit = stockMovementPort.createAndLogAudit(stockMovement);
        if (logAudit.isEmpty()) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}]. Error occured when registering the stock movement of product [{}]", errorId, employeeNumber, productCode);
            throw new ServiceException(errorId, "Technical error occured when registering the stock movement.");
        }

        return domainArticle;
    }


}
