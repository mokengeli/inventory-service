package com.bacos.mokengeli.biloko.presentation.controller;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.service.ProductService;
import org.springframework.http.ResponseEntity;
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
    @PostMapping
    public ResponseEntity<DomainProduct> createProduct(@RequestBody DomainProduct product) {
        DomainProduct createdProduct = productService.createProduct(product);
        return ResponseEntity.ok(createdProduct);
    }

    // Récupérer un produit par son ID
    @GetMapping("/{productId}")
    public ResponseEntity<DomainProduct> getProductById(@PathVariable Long productId) {
        DomainProduct product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    // Mettre à jour un produit
    @PutMapping("/{productId}")
    public ResponseEntity<DomainProduct> updateProduct(
            @PathVariable Long productId,
            @RequestBody DomainProduct productDetails) {
        DomainProduct updatedProduct = productService.updateProduct(productId, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    // Supprimer un produit
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // Lister tous les produits pour un tenant
    @GetMapping
    public ResponseEntity<List<DomainProduct>> getAllProducts(@RequestParam String tenantCode) {
        List<DomainProduct> products = productService.getAllProductsForTenant(tenantCode);
        return ResponseEntity.ok(products);
    }
}
