package com.bacos.mokengeli.biloko.application.service;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductPort productPort;

    public ProductService(ProductPort productPort) {
        this.productPort = productPort;
    }

    public DomainProduct createProduct(DomainProduct product) {
        return productPort.addProduct(product);
    }

    public DomainProduct getProductById(Long productId) {
        return productPort.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public DomainProduct updateProduct(Long productId, DomainProduct productDetails) {
        DomainProduct existingProduct = getProductById(productId);
        existingProduct.setName(productDetails.getName());
        //existingProduct.setCategory(productDetails.getCategory());
        //existingProduct.setDescription(productDetails.getDescription());
        return productPort.addProduct(existingProduct);
    }

    public void deleteProduct(Long productId) {
        productPort.deleteById(productId);
    }

    public List<DomainProduct> getAllProductsForTenant(String tenantCode) {
        return productPort.findAllByTenantCode(tenantCode);
    }
}
