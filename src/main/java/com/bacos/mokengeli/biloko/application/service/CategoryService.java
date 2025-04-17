package com.bacos.mokengeli.biloko.application.service;


import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.application.domain.model.ConnectedUser;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.port.CategoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CategoryService {
    private final CategoryPort categoryPort;
    private final UserAppService userAppService;

    @Autowired
    public CategoryService(CategoryPort categoryPort, UserAppService userAppService) {
        this.categoryPort = categoryPort;
        this.userAppService = userAppService;
    }


    public DomainCategory createCategory(DomainCategory category) throws ServiceException {
        try {
            return categoryPort.addCategory(category);
        } catch (ServiceException e) {
            ConnectedUser connectedUser = this.userAppService.getConnectedUser();
            log.error("[{}]: User [{}]. {}", e.getTechnicalId(),
                    connectedUser.getEmployeeNumber(), e.getMessage());
            throw new ServiceException(e.getTechnicalId(), e.getMessage());
        }
    }

    public DomainCategory getCategoryById(Long categoryId) throws ServiceException {
        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String employeeNumber = connectedUser.getEmployeeNumber();


        return categoryPort.findById(categoryId)
                .orElseThrow(() -> {
                    String errorId = UUID.randomUUID().toString();
                    String errorMsg = "Category id = " + categoryId + " not found";
                    log.error("[{}]: User [{}]. {}", errorId, employeeNumber, errorMsg);
                    return new ServiceException(errorId, "Technical Error");
                });
    }

    public List<DomainCategory> getAllCategories() {
        return categoryPort.findAll().orElse(new ArrayList<>());

    }
}
