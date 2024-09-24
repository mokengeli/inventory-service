package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.port.StockMovementPort;
import com.bacos.mokengeli.biloko.infrastructure.model.Article;
import com.bacos.mokengeli.biloko.infrastructure.model.StockAuditLog;
import com.bacos.mokengeli.biloko.infrastructure.model.StockMovement;
import com.bacos.mokengeli.biloko.infrastructure.repository.ArticleRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.StockAuditLogRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.StockMovementRepository;
import com.bacos.mokengeli.biloko.infrastructure.mapper.StockMovementMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StockMovementAdapter implements StockMovementPort {

    private final StockMovementRepository stockMovementRepository;
    private final ArticleRepository articleRepository;
    private final StockAuditLogRepository stockAuditLogRepository;

    public StockMovementAdapter(StockMovementRepository stockMovementRepository, ArticleRepository articleRepository, StockAuditLogRepository stockAuditLogRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.articleRepository = articleRepository;
        this.stockAuditLogRepository = stockAuditLogRepository;
    }

    @Override
    public DomainStockMovement createAndLogAudit(DomainStockMovement domainStockMovement) {
        Long articleId = domainStockMovement.getArticleId();
        Article article = this.articleRepository.findById(articleId)
                .orElseThrow();
        StockMovement stockMovement = StockMovementMapper.toEntity(domainStockMovement);
        String unitOfMeasure = article.getProduct().getUnitOfMeasure().getName();
        stockMovement.setDescription("Ajout de " + domainStockMovement.getTotalVolume() +
                " [" + unitOfMeasure + "] pour le produit ID " + article.getProduct().getCode());
        stockMovement.setArticle(article);
        stockMovement = stockMovementRepository.save(stockMovement);

        return StockMovementMapper.toDomain(stockMovement);  // Returning saved entity mapped to DomainStockMovement
    }


}
