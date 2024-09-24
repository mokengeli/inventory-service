
package com.bacos.mokengeli.biloko.infrastructure.mapper;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.infrastructure.model.Product;
import com.bacos.mokengeli.biloko.infrastructure.model.UnitOfMeasure;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductMapper {

    public DomainProduct toDomain(Product product) {
        return DomainProduct.builder()
                .id(product.getId())
                .tenantCode(product.getTenantCode())
                .code(product.getCode())
                .name(product.getName())
                .volume(product.getVolume())
                .category(CategoryMapper.toDomain(product.getCategory()))
                .unitOfMeasure(product.getUnitOfMeasure().getName())
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
                .category(CategoryMapper.toEntity(domainProduct.getCategory()))
                .volume(domainProduct.getVolume())
                .description(domainProduct.getDescription())
                .createdAt(domainProduct.getCreatedAt())
                .updatedAt(domainProduct.getUpdatedAt())
                .build();
    }
}

