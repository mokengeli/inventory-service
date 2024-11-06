package com.bacos.mokengeli.biloko.application.service;

import com.bacos.mokengeli.biloko.application.domain.*;
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
import java.util.ArrayList;
import java.util.List;
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
    public DomainArticle addArticleToInventory(Long productId, int numberOfUnits) throws ServiceException {

        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String employeeNumber = connectedUser.getEmployeeNumber();
        // Récupérer le produit via le port en filtrant par productId et tenantId
        Optional<DomainProduct> optProduct = productPort.findById(productId);
        if (optProduct.isEmpty()) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}] try to add article with non existing product id [{}]", errorId, employeeNumber, productId);
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
        double quantityToAdd = product.getVolume() * numberOfUnits;

        // Vérifier s'il existe déjà un article pour ce produit
        Optional<DomainArticle> existingArticleOpt = articlePort.findByProductId(product.getId());
        double oldQuantity = 0;
        DomainArticle domainArticle;
        if (existingArticleOpt.isPresent()) {
            // Mettre à jour le volume total de l'article existant
            DomainArticle existingArticle = existingArticleOpt.get();
            double actualQuantity = existingArticle.getQuantity();
            oldQuantity = actualQuantity;
            if (actualQuantity < 0) {
                actualQuantity = 0;
            }
            double updatedTotalVolume = actualQuantity + quantityToAdd;
            domainArticle = DomainArticle.builder()
                    .id(existingArticle.getId())
                    .productId(existingArticle.getProductId())
                    .quantity(updatedTotalVolume)
                    .createdAt(existingArticle.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            // Créer un nouvel article
            domainArticle = DomainArticle.builder()
                    .productId(product.getId())
                    .quantity(quantityToAdd)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
        // Sauvegarder l'article via le port
        Optional<DomainArticle> optionalDomainArticle = articlePort.save(domainArticle);
        if (optionalDomainArticle.isEmpty()) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}]. Something went wrong while adding article of product code [{}] ", errorId, employeeNumber, productId);
            throw new ServiceException(errorId, "Something went wrong while adding the product.");
        }

        domainArticle = optionalDomainArticle.get();

        String observation = "Ajout de " + quantityToAdd +
                " " + product.getUnitOfMeasure() + " pour le produit - " + product.getName();

        // Créer un mouvement de stock de type ENTREE
        DomainStockMovement stockMovement = DomainStockMovement.builder()
                .articleId(domainArticle.getId())
                .movementType(MovementTypeEnum.ADD_ARTICLE.name())
                .employeeNumber(employeeNumber)
                .oldQuantity(oldQuantity)
                .quantityMoved(quantityToAdd)
                .newQuantity(domainArticle.getQuantity())
                .observation(observation)
                .unitOfMeasure(product.getUnitOfMeasure())
                .movementDate(LocalDateTime.now())
                .build();


        Optional<DomainStockMovement> logAudit = stockMovementPort.createAndLogAudit(stockMovement);
        if (logAudit.isEmpty()) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}]. Error occured when registering the stock movement of product [{}]", errorId, employeeNumber, productId);
            throw new ServiceException(errorId, "Technical error occured when registering the stock movement.");
        }

        return domainArticle;
    }

    @Transactional
    public List<DomainArticle> removeArticleToInventory(List<DomainActionArticle> removeProductRequests) throws ServiceException {
        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String tenantCode = connectedUser.getTenantCode();
        List<Long> productIds = removeProductRequests.stream().map(DomainActionArticle::getProductId).toList();
        boolean allProductOfTenantCode = this.productPort.isAllProductOfTenantCode(productIds, tenantCode);
        String employeeNumber = connectedUser.getEmployeeNumber();
        if (!allProductOfTenantCode) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}] of tenant [{}] try to get product of another tenant. Product Ids [{}] to the stock", errorId,
                    employeeNumber, connectedUser.getTenantCode(), productIds);
            throw new ServiceException(errorId, "You can't add item owning by another partener");
        }
        List<DomainArticle> domainArticles = new ArrayList<>();
        List<DomainStockMovement> domainStockMovements = new ArrayList<>();
        for (DomainActionArticle domainActionArticle : removeProductRequests) {
            Long productId = domainActionArticle.getProductId();
            Optional<DomainProduct> optProduct = productPort.findById(productId);
            if (optProduct.isEmpty()) {
                String errorId = UUID.randomUUID().toString();
                log.error("[{}]: User [{}]. No Product found for product id [{}]", errorId, employeeNumber, productId);
                throw new ServiceException(errorId, "No Product found for some product id = " + productId);
            }

            Optional<DomainArticle> existingArticleOpt = articlePort.findByProductId(productId);
            if (existingArticleOpt.isEmpty()) {
                String errorId = UUID.randomUUID().toString();
                log.error("[{}]: User [{}]. No article found for product id [{}]", errorId, employeeNumber, productId);
                throw new ServiceException(errorId, "No article found for some product id = " + productId);
            }
            DomainArticle existingArticle = existingArticleOpt.get();
            double oldQuantity = existingArticle.getQuantity();


            double updatedTotalVolume = oldQuantity - domainActionArticle.quantity;
            String observation = "";
            DomainProduct domainProduct = optProduct.get();
            if (updatedTotalVolume < 0) {
                String warningId = UUID.randomUUID().toString();
                log.warn("[{}]: User [{}].  Article [{}] has negative quantity. The stock must be adjusted", warningId,
                        employeeNumber, existingArticle.getId());
                observation = "Le stock est vide après le retrait de " +
                        domainActionArticle.getQuantity() +
                        " " + domainProduct.getUnitOfMeasure() + " pour le produit - " + domainProduct.getName();
                updatedTotalVolume = 0;
            } else {
                observation = "Retrait de " + domainActionArticle.getQuantity() +
                        " " + domainProduct.getUnitOfMeasure() + " pour le produit - " + domainProduct.getName();
            }
            existingArticle.setUpdatedAt(LocalDateTime.now());
            existingArticle.setQuantity(updatedTotalVolume);
            domainArticles.add(existingArticle);


            DomainStockMovement stockMovement = DomainStockMovement.builder()
                    .articleId(existingArticle.getId())
                    .movementType(MovementTypeEnum.REMOVE_ARTICLE.name())
                    .employeeNumber(employeeNumber)
                    .oldQuantity(oldQuantity)
                    .quantityMoved(domainActionArticle.getQuantity())
                    .newQuantity(updatedTotalVolume)
                    .unitOfMeasure(domainProduct.getUnitOfMeasure())
                    .observation(observation)
                    .movementDate(LocalDateTime.now())
                    .build();

            domainStockMovements.add(stockMovement);
        }
        try {
            // save new quantity of All articles
            Optional<List<DomainArticle>> domainArticles1 = this.articlePort.saveAll(domainArticles);
            if (domainArticles1.isEmpty()) {
                String errorId = UUID.randomUUID().toString();
                log.error("[{}]: User [{}]. Unexpected Error Occured while saving all articles", errorId, employeeNumber);
                throw new ServiceException(errorId, "Unexpected Error Occured while saving all articles");
            }
            // audit the action
            this.stockMovementPort.createAndLogAuditList(domainStockMovements);
            return domainArticles1.get();
        } catch (ServiceException e) {

            log.error("[{}]: User [{}]. {}", e.getTechnicalId(),
                    connectedUser.getEmployeeNumber(), e.getMessage());
            throw new ServiceException(e.getTechnicalId(), "Technical Error Occured");
        }


    }
}
