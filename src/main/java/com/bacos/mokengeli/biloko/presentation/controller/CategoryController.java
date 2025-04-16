package com.bacos.mokengeli.biloko.presentation.controller;

import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.service.CategoryService;
import com.bacos.mokengeli.biloko.presentation.exception.ResponseStatusWrapperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/inventory/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasAuthority('EDIT_INVENTORY')")
    @PostMapping
    public ResponseEntity<DomainCategory> createCategory(@RequestBody DomainCategory category) {
        try {
            DomainCategory domainCategory = categoryService.createCategory(category);
            return ResponseEntity.ok(domainCategory);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }

    @PreAuthorize("hasAnyAuthority('VIEW_INVENTORY','EDIT_INVENTORY')")
    @GetMapping("/{categoryId}")
    public ResponseEntity<DomainCategory> getCategory(@PathVariable Long categoryId) {
        try {
            DomainCategory domainCategory = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(domainCategory);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }

    @PreAuthorize("hasAnyAuthority('VIEW_INVENTORY','EDIT_INVENTORY')")
    @GetMapping("/all")
    public ResponseEntity<List<DomainCategory>> getAllCategories() {
        List<DomainCategory> domainCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(domainCategories);
    }
}
