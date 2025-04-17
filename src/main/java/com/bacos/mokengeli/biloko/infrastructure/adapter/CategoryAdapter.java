package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.application.domain.model.ConnectedUser;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.port.CategoryPort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.CategoryMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.Category;
import com.bacos.mokengeli.biloko.infrastructure.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CategoryAdapter implements CategoryPort {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryAdapter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public DomainCategory addCategory(DomainCategory category) throws ServiceException {
        try {
            Category cat = CategoryMapper.toEntity(category);
            cat.setCreatedAt(LocalDateTime.now());

            Category save = this.categoryRepository.save(cat);
            return CategoryMapper.toDomain(save);
        } catch (DataIntegrityViolationException e) {
            String uuid = UUID.randomUUID().toString();

            throw new ServiceException(uuid, "La category " + category.getName() + " existe deja.");
        }
    }

    @Override
    public Optional<DomainCategory> findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(CategoryMapper::toDomain);
    }

    @Override
    public Optional<List<DomainCategory>> findAll() {
        List<Category> all = categoryRepository.findAll();
        if (all.isEmpty()) {
            return Optional.empty();
        }
        List<DomainCategory> domainCategories = new ArrayList<>();
        all.forEach(
                category -> domainCategories.add(CategoryMapper.toDomain(category))
        );
        return Optional.of(domainCategories);
    }
}
