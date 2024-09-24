package com.bacos.mokengeli.biloko.presentation.controller;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.service.ArticleService;
import com.bacos.mokengeli.biloko.presentation.model.ArticleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Créer un nouvel article pour un produit
    @PostMapping("/add")
    public ResponseEntity<DomainArticle> addArticle(@RequestBody ArticleRequest articleRequest) {
        DomainArticle createdArticle = articleService.addArticleToInventory(articleRequest.getProductCode(),
                articleRequest.getNumberOfUnits());
        return ResponseEntity.ok(createdArticle);
    }




}
