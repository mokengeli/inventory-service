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


}
