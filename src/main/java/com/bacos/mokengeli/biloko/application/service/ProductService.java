package com.bacos.mokengeli.biloko.application.service;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.domain.model.ConnectedUser;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ProductService {

    private final ProductPort productPort;
    private final UserAppService userAppService;


    public ProductService(ProductPort productPort, UserAppService userAppService) {
        this.productPort = productPort;
        this.userAppService = userAppService;
    }

    public DomainProduct createProduct(DomainProduct product) throws ServiceException {
        try {
            productPort.addProduct(product);
        } catch (ServiceException e) {
            ConnectedUser connectedUser = this.userAppService.getConnectedUser();
            log.error("[{}]: User [{}]. {}", e.getTechnicalId(),
                    connectedUser.getEmployeeNumber(), e.getMessage());
            throw new ServiceException(e.getTechnicalId(), "Technical Error");
        }
        return null;
    }

    public DomainProduct getProductById(Long productId) throws ServiceException {
        Optional<DomainProduct> optProduct = productPort.findById(productId);
        if (optProduct.isEmpty()) {
            ConnectedUser connectedUser = this.userAppService.getConnectedUser();
            String errorId = UUID.randomUUID().toString();
            String errorMsg = "Product id = " + productId + " not Found";
            log.error("[{}]: User [{}]. {}", errorId, connectedUser.getEmployeeNumber(), errorMsg);
            throw new ServiceException(errorId, "Technical Error");

        }
        return optProduct.get();
    }


}
