
package com.bacos.mokengeli.biloko.infrastructure.mapper;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.infrastructure.model.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductMapper {

    public DomainProduct toDomain(Product product) {
        return DomainProduct.builder()
                .id(product.getId())
                .tenantCode(product.getTenantCode())
                .name(product.getName())
                .category(product.getCategory().getName())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public Product toEntity(DomainProduct domainProduct) {
        return Product.builder()
                .id(domainProduct.getId())
                .tenantCode(domainProduct.getTenantCode())
                .name(domainProduct.getName())
                //.category(domainProduct.getCategory())
                .description(domainProduct.getDescription())
                .createdAt(domainProduct.getCreatedAt())
                .updatedAt(domainProduct.getUpdatedAt())
                .build();
    }
}

