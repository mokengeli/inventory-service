package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryPort {
    DomainCategory addCategory(DomainCategory category) throws ServiceException;

    Optional<DomainCategory> findById(Long categoryId);

    Page<DomainCategory> findAll(int page, int size);}
