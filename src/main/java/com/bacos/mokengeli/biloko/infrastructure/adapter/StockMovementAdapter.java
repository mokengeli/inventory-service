package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.port.StockMovementPort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.StockMovementMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.Article;
import com.bacos.mokengeli.biloko.infrastructure.model.StockMovement;
import com.bacos.mokengeli.biloko.infrastructure.repository.ArticleRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.StockMovementRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
        StockMovement stockMovement = StockMovementMapper.toEntity(domainStockMovement);
        Long articleId = domainStockMovement.getArticleId();
        if (articleId != null) {
            Optional<Article> optArticle = this.articleRepository.findById(articleId);
            if (optArticle.isEmpty()) {
                return Optional.empty();
            }
            Article article = optArticle.get();
            stockMovement.setArticle(article);
        }
        stockMovement = stockMovementRepository.save(stockMovement);
        return Optional.of(StockMovementMapper.toDomain(stockMovement));  // Returning saved entity mapped to DomainStockMovement
    }

    @Override
    public Optional<List<DomainStockMovement>> createAndLogAuditList(List<DomainStockMovement> domainStockMovements) {
        List<DomainStockMovement> domainStockMovementList = new ArrayList<>();
        for (DomainStockMovement domainStockMovement : domainStockMovements) {
            domainStockMovement = this.createAndLogAudit(domainStockMovement)
                    .orElseThrow(() -> new RuntimeException("Domain stock movement not created"));

            domainStockMovementList.add(domainStockMovement);
        }

        return Optional.of(domainStockMovementList);
    }


}
