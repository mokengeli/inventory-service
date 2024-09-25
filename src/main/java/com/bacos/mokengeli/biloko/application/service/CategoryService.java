package com.bacos.mokengeli.biloko.application.service;


import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.application.domain.model.ConnectedUser;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.port.CategoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new ServiceException(e.getTechnicalId(), "Technical Error");
        }
    }
}
