package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface ArticlePort {

    Optional<DomainArticle> save(DomainArticle article);

    Optional<DomainArticle> findByProductId(Long productId);

    Optional<List<DomainArticle>>  saveAll(List<DomainArticle> domainArticles) throws ServiceException;
}
