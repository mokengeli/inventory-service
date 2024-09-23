package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;

import java.util.List;
import java.util.Optional;

public interface ArticlePort {

    DomainArticle save(DomainArticle article);

    Optional<DomainArticle> findById(Long id);

    Optional<DomainArticle> findByProductId(Long productId);

    void deleteById(Long id);

    List<DomainArticle> findAllByProductId(Long productId);
}
