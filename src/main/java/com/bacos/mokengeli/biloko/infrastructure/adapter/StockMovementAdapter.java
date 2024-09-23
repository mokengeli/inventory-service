package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.port.StockMovementPort;
import com.bacos.mokengeli.biloko.infrastructure.model.Article;
import com.bacos.mokengeli.biloko.infrastructure.model.StockMovement;
import com.bacos.mokengeli.biloko.infrastructure.repository.ArticleRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.ProductRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.StockMovementRepository;
import com.bacos.mokengeli.biloko.infrastructure.mapper.StockMovementMapper;
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
    public DomainStockMovement save(DomainStockMovement stockMovement) {
        Long articleId = stockMovement.getArticleId();
        Article article = this.articleRepository.findById(articleId)
                .orElseThrow();
        StockMovement entity = StockMovementMapper.toEntity(stockMovement);  // Mapping DomainStockMovement to StockMovement
        entity.setArticle(article);
        entity = stockMovementRepository.save(entity);
        return StockMovementMapper.toDomain(entity);  // Returning saved entity mapped to DomainStockMovement
    }
}
