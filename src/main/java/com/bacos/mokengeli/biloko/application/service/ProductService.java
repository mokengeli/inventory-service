package com.bacos.mokengeli.biloko.application.service;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.domain.model.ConnectedUser;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.port.ArticlePort;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Slf4j
@Service
public class ProductService {

    private final ProductPort productPort;
    private final UserAppService userAppService;
    private final ArticlePort articlePort;


    public ProductService(ProductPort productPort, UserAppService userAppService, ArticlePort articlePort) {
        this.productPort = productPort;
        this.userAppService = userAppService;
        this.articlePort = articlePort;
    }

    public DomainProduct createProduct(DomainProduct product) throws ServiceException {
        try {
            Optional<DomainProduct> domainProduct = productPort.addProduct(product);
            if (domainProduct.isEmpty()) {
                ConnectedUser connectedUser = this.userAppService.getConnectedUser();
                String uuid = UUID.randomUUID().toString();
                log.error("[{}]: User [{}]. {}", uuid,
                        connectedUser.getEmployeeNumber(), "Aucun produit créé");
                throw new ServiceException(uuid, "Aucun produit n'a été créé");
            }
            return domainProduct.get();
        } catch (ServiceException e) {
            ConnectedUser connectedUser = this.userAppService.getConnectedUser();
            log.error("[{}]: User [{}]. {}", e.getTechnicalId(),
                    connectedUser.getEmployeeNumber(), e.getMessage());
            throw new ServiceException(e.getTechnicalId(), e.getMessage());
        }
    }

    public DomainProduct getProductById(Long productId) throws ServiceException {
        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String tenantCode = connectedUser.getTenantCode();
        String employeeNumber = connectedUser.getEmployeeNumber();

        List<Long> ids = Collections.singletonList(productId);

        if (!this.productPort.isAllProductOfTenantCode(ids, tenantCode)) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}] of tenant [{}] try to get product of tenant. Product id [{}] to the stock", errorId,
                    employeeNumber, connectedUser.getTenantCode(), productId);
            throw new ServiceException(errorId, "You can't add item owning by another partener");
        }

        Optional<DomainProduct> optProduct = productPort.findById(productId);
        if (optProduct.isEmpty()) {
            String errorId = UUID.randomUUID().toString();
            String errorMsg = "Product id = " + productId + " not Found";
            log.error("[{}]: User [{}]. {}", errorId, connectedUser.getEmployeeNumber(), errorMsg);
            throw new ServiceException(errorId, "Technical Error");
        }

        Optional<DomainArticle> optArticle = this.articlePort.findByProductId(productId);
        DomainProduct domainProduct = optProduct.get();
        if (optArticle.isEmpty()) {
            return domainProduct;
        }
        DomainArticle domainArticle = optArticle.get();
        domainProduct.setArticle(domainArticle);
        return domainProduct;
    }


    public List<DomainProduct> getProductByIds(List<Long> productIds) throws ServiceException {
        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String tenantCode = connectedUser.getTenantCode();

        if (!this.productPort.isAllProductOfTenantCode(productIds, tenantCode)) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}] of tenant [{}] try to get product of another tenant. Product Ids [{}] to the stock", errorId,
                    connectedUser.getEmployeeNumber(), connectedUser.getTenantCode(), productIds);
            throw new ServiceException(errorId, "You can't add item owning by another partener");
        }
        Optional<List<DomainProduct>> products = this.productPort.findByIds(productIds);
        return products.orElseGet(ArrayList::new);
    }

    public boolean isProductExistAndOfTheSomeOrganisation(List<Long> idsProduct) {
        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String tenantCode = connectedUser.getTenantCode();
        return this.productPort.isAllProductOfTenantCode(idsProduct, tenantCode);
    }

    public List<DomainProduct> getAllProductsByOrganisation() {

        if (this.userAppService.isAdminUser()) {
            Optional<List<DomainProduct>> domainProducts = this.productPort.getAllProducts();
            return domainProducts.orElse(Collections.emptyList());
        }
        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String tenantCode = connectedUser.getTenantCode();
        Optional<List<DomainProduct>> domainProducts = this.productPort.getAllProductsByTenant(tenantCode);
        return domainProducts.orElse(Collections.emptyList());
    }

    public Set<String> getAllUnitOfMeasurement() {

        return this.productPort.getAllUnitOfMeasurement();
    }


}
