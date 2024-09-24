package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.application.port.CategoryPort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.CategoryMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.Category;
import com.bacos.mokengeli.biloko.infrastructure.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryAdapter implements CategoryPort {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryAdapter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public DomainCategory addCategory(DomainCategory category) {
        Category cat = CategoryMapper.toEntity(category);
        cat.setCreatedAt(LocalDateTime.now());
        Category save = this.categoryRepository.save(cat);
        return CategoryMapper.toDomain(save);
    }
}
