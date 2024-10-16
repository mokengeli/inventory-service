package com.bacos.mokengeli.biloko.presentation.controller;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.service.ArticleService;
import com.bacos.mokengeli.biloko.presentation.exception.ResponseStatusWrapperException;
import com.bacos.mokengeli.biloko.presentation.model.ProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Créer un nouvel article pour un produit
    @PreAuthorize("hasAuthority('EDIT_INVENTORY')")
    @PostMapping("/add")
    public ResponseEntity<DomainArticle> addArticle(@RequestBody ProductRequest productRequest) {
        try {
            DomainArticle createdArticle = articleService.addArticleToInventory(productRequest.getProductCode(),
                    productRequest.getNumberOfUnits());
            return ResponseEntity.ok(createdArticle);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }


}
