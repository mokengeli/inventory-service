package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;

import java.util.List;
import java.util.Optional;

public interface ArticlePort {

    Optional<DomainArticle> save(DomainArticle article);

    Optional<DomainArticle> findByProductId(Long productId);

}
