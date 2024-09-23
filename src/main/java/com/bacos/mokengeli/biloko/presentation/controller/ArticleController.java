package com.bacos.mokengeli.biloko.presentation.controller;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Créer un nouvel article pour un produit
    @PostMapping("/add")
    public ResponseEntity<DomainArticle> addArticle(@RequestBody ArticleRequest articleRequest) {
        DomainArticle createdArticle = articleService.addArticleToInventory(articleRequest.getProductId(),
                articleRequest.getNumberOfUnits());
        return ResponseEntity.ok(createdArticle);
    }

    // Récupérer un article par ID
    @GetMapping("/{articleId}")
    public ResponseEntity<DomainArticle> getArticleById(@PathVariable Long articleId) {
        DomainArticle article = articleService.getArticleById(articleId);
        return ResponseEntity.ok(article);
    }

    // Lister les articles pour un produit
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<DomainArticle>> getAllArticlesByProductId(@PathVariable Long productId) {
        List<DomainArticle> articles = articleService.getAllArticlesByProductId(productId);
        return ResponseEntity.ok(articles);
    }

    // Supprimer un article
    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.noContent().build();
    }
}
