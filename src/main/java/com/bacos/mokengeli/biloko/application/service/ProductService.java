package com.bacos.mokengeli.biloko.application.service;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.domain.model.ConnectedUser;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.port.ArticlePort;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
            ConnectedUser connectedUser = this.userAppService.getConnectedUser();
            String tenantCode = product.getTenantCode();
            if (!this.userAppService.isAdminUser() &&
                    !connectedUser.getTenantCode().equals(tenantCode)) {
                String uuid = UUID.randomUUID().toString();
                log.error("[{}]: User [{}] Tenant [{}] try to create product {} to another tenant: {}", uuid,
                        connectedUser.getEmployeeNumber(), connectedUser.getTenantCode(), product.getName(), tenantCode);

                throw new ServiceException(uuid, "You don't have permission to perfom this action");
            }

            Optional<DomainProduct> domainProduct = productPort.addProduct(product);

            if (domainProduct.isEmpty()) {
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

        if (!this.userAppService.isAdminUser()
                && !this.productPort.isAllProductOfTenantCode(ids, tenantCode)) {
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

        if (!this.userAppService.isAdminUser() &&
                !this.productPort.isAllProductOfTenantCode(productIds, tenantCode)) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}] of tenant [{}] try to get product of another tenant. Product Ids [{}] to the stock", errorId,
                    connectedUser.getEmployeeNumber(), connectedUser.getTenantCode(), productIds);
            throw new ServiceException(errorId, "You can't add item owning by another partener");
        }
        Optional<List<DomainProduct>> products = this.productPort.findByIds(productIds);
        return products.orElseGet(ArrayList::new);
    }

    public boolean isProductExistAndOfTheSomeOrganisation(String tenantCode, List<Long> idsProduct) throws ServiceException {
        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String tenantCodeUser = connectedUser.getTenantCode();


        if (!this.userAppService.isAdminUser() && !tenantCodeUser.equals(tenantCode)) {
            String employeeNumber = connectedUser.getEmployeeNumber();
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}] of tenant [{}] try to check products of another tenant  [{}]", errorId,
                    employeeNumber, connectedUser.getTenantCode(), tenantCode);
            throw new ServiceException(errorId, "You can't get products item owning by another partener");

        }
        return this.productPort.isAllProductOfTenantCode(idsProduct, tenantCode);
    }

    public Page<DomainProduct> getAllProductsByOrganisation(String tenantCode, int page, int size) throws ServiceException {
        ConnectedUser connectedUser = this.userAppService.getConnectedUser();
        String employeeNumber = connectedUser.getEmployeeNumber();
        if (!this.userAppService.isAdminUser() && !connectedUser.getTenantCode().equals(tenantCode)) {
            String errorId = UUID.randomUUID().toString();
            log.error("[{}]: User [{}] of tenant [{}] try to get all products of another tenant [{}]", errorId,
                    employeeNumber, connectedUser.getTenantCode(), tenantCode);
            throw new ServiceException(errorId, "You can't get products item owning by another partner");
        }
        return productPort.getAllProductsByTenant(tenantCode, page, size);
    }

    public Set<String> getAllUnitOfMeasurement() {

        return this.productPort.getAllUnitOfMeasurement();
    }


}
