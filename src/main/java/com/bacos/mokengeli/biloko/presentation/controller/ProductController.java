package com.bacos.mokengeli.biloko.presentation.controller;

import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.service.ProductService;
import com.bacos.mokengeli.biloko.presentation.exception.ResponseStatusWrapperException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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


    @PreAuthorize("hasAnyAuthority('VIEW_INVENTORY','EDIT_INVENTORY')")
    @GetMapping("/{productId}")
    public ResponseEntity<DomainProduct> getProductById(@PathVariable Long productId) {
        try {
            DomainProduct product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }

    @PreAuthorize("hasAnyAuthority('VIEW_INVENTORY','EDIT_INVENTORY')")
    @GetMapping("/by-ids")
    public List<DomainProduct> getProductByIds(@RequestParam("ids") List<Long> idsProduct) {
        try {
            return productService.getProductByIds(idsProduct);

        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }

    @GetMapping("/exist-in-orga")
    public boolean isProductExistAndOfTheSomeOrganisation(@RequestParam("tenant") String tenantCode,
                                                          @RequestParam("ids") List<Long> idsProduct) {
        try {
            return productService.isProductExistAndOfTheSomeOrganisation(tenantCode, idsProduct);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }
    }

    @PreAuthorize("hasAnyAuthority('VIEW_INVENTORY','EDIT_INVENTORY')")
    @GetMapping("/all")
    public ResponseEntity<Page<DomainProduct>> getAllProduct(
            @RequestParam("code") String tenantCode,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Page<DomainProduct> domainProducts = productService.getAllProductsByOrganisation(tenantCode, page, size);
            return ResponseEntity.ok(domainProducts);
        } catch (ServiceException e) {
            throw new ResponseStatusWrapperException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getTechnicalId());
        }

    }

    @PreAuthorize("hasAnyAuthority('VIEW_INVENTORY','EDIT_INVENTORY')")
    @GetMapping("/unitm/all")
    public ResponseEntity<Set<String>> getAllUnitOfMeasurement() {
        Set<String> unitOfM = productService.getAllUnitOfMeasurement();
        return ResponseEntity.ok(unitOfM);
    }

}
