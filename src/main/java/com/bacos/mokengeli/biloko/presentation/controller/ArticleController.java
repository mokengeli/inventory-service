package com.bacos.mokengeli.biloko.presentation.controller;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.domain.DomainActionArticle;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.service.ArticleService;
import com.bacos.mokengeli.biloko.presentation.exception.ResponseStatusWrapperException;
import com.bacos.mokengeli.biloko.presentation.model.ProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('EDIT_INVENTORY','ADD_INVENTORY')")
    @PutMapping("/add")
    public ResponseEntity<DomainArticle> addArticle(@RequestBody ProductRequest productRequest) {
        try {
            DomainArticle createdArticle = articleService.addArticleToInventory(productRequest.getProductId(),
                    productRequest.getNumberOfUnits());
            return ResponseEntity.ok(createdArticle);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }
    @PreAuthorize("hasAnyAuthority('EDIT_INVENTORY','REMOVE_INVENTORY')")
    @PutMapping("/remove")
    public List<DomainArticle> removeProduct(@RequestBody List<DomainActionArticle> removeProductRequests) {
        try {
            return this.articleService.removeArticleToInventory(removeProductRequests);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }


}
