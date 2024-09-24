package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.port.ArticlePort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.ArticleMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.Article;
import com.bacos.mokengeli.biloko.infrastructure.model.Product;
import com.bacos.mokengeli.biloko.infrastructure.repository.ArticleRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public DomainArticle save(DomainArticle domainArticle) {
        Product product = this.productRepository.findById(domainArticle.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Article article = ArticleMapper.toEntity(domainArticle);
        article.setProduct(product);
        Article savedArticle = articleRepository.save(article);
        return ArticleMapper.toDomain(savedArticle);
    }

    @Override
    public Optional<DomainArticle> findById(Long id) {
        return articleRepository.findById(id)
                .map(ArticleMapper::toDomain);
    }

    @Override
    public Optional<DomainArticle> findByProductId(Long productId) {
        Optional<Article> article = articleRepository.findByProductId(productId);
        return article.map(ArticleMapper::toDomain);  // Mapping Article to DomainArticle
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public List<DomainArticle> findAllByProductId(Long productId) {
        return articleRepository.findAllByProductId(productId)
                .stream()
                .map(ArticleMapper::toDomain)
                .collect(Collectors.toList());
    }
}
