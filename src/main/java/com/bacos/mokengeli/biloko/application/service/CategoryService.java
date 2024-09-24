package com.bacos.mokengeli.biloko.application.service;


import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.application.port.CategoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryPort categoryPort;

    @Autowired
    public CategoryService(CategoryPort categoryPort) {
        this.categoryPort = categoryPort;
    }


    public DomainCategory createCategory(DomainCategory category) {
        return categoryPort.addCategory(category);
    }
}
