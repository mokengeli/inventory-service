package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.port.StockMovementPort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.StockMovementMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.Article;
import com.bacos.mokengeli.biloko.infrastructure.model.StockMovement;
import com.bacos.mokengeli.biloko.infrastructure.repository.ArticleRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.StockMovementRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StockMovementAdapter implements StockMovementPort {

    private final StockMovementRepository stockMovementRepository;
    private final ArticleRepository articleRepository;

    public StockMovementAdapter(StockMovementRepository stockMovementRepository, ArticleRepository articleRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Optional<DomainStockMovement> createAndLogAudit(DomainStockMovement domainStockMovement) {
        Long articleId = domainStockMovement.getArticleId();
        Optional<Article> optArticle = this.articleRepository.findById(articleId);
        if (optArticle.isEmpty()) {
            return Optional.empty();
        }
        Article article = optArticle.get();
        StockMovement stockMovement = StockMovementMapper.toEntity(domainStockMovement);
        String unitOfMeasure = article.getProduct().getUnitOfMeasure().getName();
        stockMovement.setDescription("Ajout de " + domainStockMovement.getTotalVolume() +
                " [" + unitOfMeasure + "] pour le produit de code " + article.getProduct().getCode());
        stockMovement.setArticle(article);
        stockMovement = stockMovementRepository.save(stockMovement);

        return Optional.of(StockMovementMapper.toDomain(stockMovement));  // Returning saved entity mapped to DomainStockMovement
    }


}
