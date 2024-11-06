package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.port.ArticlePort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.ArticleMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.Article;
import com.bacos.mokengeli.biloko.infrastructure.model.Product;
import com.bacos.mokengeli.biloko.infrastructure.repository.ArticleRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ArticleAdapter implements ArticlePort {

    private final ArticleRepository articleRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ArticleAdapter(ArticleRepository articleRepository, ProductRepository productRepository) {
        this.articleRepository = articleRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Optional<DomainArticle> save(DomainArticle domainArticle) {

        Optional<Product> optionalProduct = this.productRepository.findById(domainArticle.getProductId());
        if (optionalProduct.isEmpty()) {
            return Optional.empty();
        }
        Product product = optionalProduct.get();
        Article article = ArticleMapper.toEntity(domainArticle);
        article.setProduct(product);
        Article savedArticle = articleRepository.save(article);
        return Optional.of(ArticleMapper.toDomain(savedArticle));
    }


    @Override
    public Optional<DomainArticle> findByProductId(Long productId) {
        Optional<Article> article = articleRepository.findByProductId(productId);
        return article.map(ArticleMapper::toDomain);  // Mapping Article to DomainArticle
    }

    @Transactional
    @Override
    public Optional<List<DomainArticle>> saveAll(List<DomainArticle> domainArticles) throws ServiceException {
        List<DomainArticle> domainArticleList = new ArrayList<>();
        for (DomainArticle domainArticle : domainArticles) {
            Long id = domainArticle.getId();
            domainArticle = this.save(domainArticle)
                    .orElseThrow(() -> new ServiceException(UUID.randomUUID().toString(), "Something went wrong while saving article with id= " + id));
            domainArticleList.add(domainArticle);
        }
        return Optional.of(domainArticleList);
    }


}
