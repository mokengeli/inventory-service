package com.bacos.mokengeli.biloko.presentation.controller;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.service.ProductService;
import com.bacos.mokengeli.biloko.presentation.exception.ResponseStatusWrapperException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Créer un nouveau produit
    @PreAuthorize("hasAuthority('EDIT_INVENTORY')")
    @PostMapping
    public ResponseEntity<DomainProduct> createProduct(@RequestBody DomainProduct product) {
        try {
            DomainProduct createdProduct = productService.createProduct(product);
            return ResponseEntity.ok(createdProduct);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }


    @PreAuthorize("hasAnyAuthority('EDIT_INVENTORY','VIEW_INVENTORY')")
    @GetMapping("/{productId}")
    public ResponseEntity<DomainProduct> getProductById(@PathVariable Long productId) {
        try {
            DomainProduct product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }

    @PreAuthorize("hasAnyAuthority('EDIT_INVENTORY','VIEW_INVENTORY')")
    @GetMapping("/by-ids")
    public List<DomainProduct> getProductByIds(@RequestParam("ids") List<Long> idsProduct) {
        try {
            //List<Long> ids = idsProduct.getIds();
            return productService.getProductByIds(idsProduct);

        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }

}
