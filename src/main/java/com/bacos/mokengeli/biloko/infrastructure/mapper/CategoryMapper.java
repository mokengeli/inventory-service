package com.bacos.mokengeli.biloko.infrastructure.mapper;

import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.infrastructure.model.Category;
import lombok.experimental.UtilityClass;


@UtilityClass
public class CategoryMapper {
    public static DomainCategory toDomain(Category category) {
        return DomainCategory.builder()
                .id(category.getId())
                .name(category.getName())
               // .tenantCode(category.getTenantCode())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public static Category toEntity(DomainCategory category) {

        return Category.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
