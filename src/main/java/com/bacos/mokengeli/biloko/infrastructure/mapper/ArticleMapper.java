package com.bacos.mokengeli.biloko.infrastructure.mapper;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.infrastructure.model.Article;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ArticleMapper {

    public DomainArticle toDomain(Article article) {
        return DomainArticle.builder()
                .id(article.getId())
                .productId(article.getProduct().getId())
                .totalVolume(article.getTotalVolume())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    public Article toEntity(DomainArticle domainArticle) {
        Article article = new Article();
        article.setId(domainArticle.getId());
        article.setTotalVolume(domainArticle.getTotalVolume());
        article.setCreatedAt(domainArticle.getCreatedAt());
        article.setUpdatedAt(domainArticle.getUpdatedAt());
        return article;
    }
}
