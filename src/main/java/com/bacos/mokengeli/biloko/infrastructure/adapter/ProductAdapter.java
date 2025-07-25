package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.ProductMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.Article;
import com.bacos.mokengeli.biloko.infrastructure.model.Product;
import com.bacos.mokengeli.biloko.infrastructure.model.UnitOfMeasure;
import com.bacos.mokengeli.biloko.infrastructure.repository.ArticleRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.ProductRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductAdapter implements ProductPort {

    private final ProductRepository productRepository;
    private final ArticleRepository articleRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    public ProductAdapter(ProductRepository productRepository, ArticleRepository articleRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.productRepository = productRepository;
        this.articleRepository = articleRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Optional<DomainProduct> addProduct(DomainProduct domainProduct) throws ServiceException {

        Product product = ProductMapper.toEntity(domainProduct);


        String unitOfMeasure = domainProduct.getUnitOfMeasure();
        UnitOfMeasure unitOfMeasure1 = this.unitOfMeasureRepository.findByName(unitOfMeasure)
                .orElseThrow(() -> new ServiceException(UUID.randomUUID().toString(), "No unit of measure found with name: " + unitOfMeasure));
        product.setCreatedAt(OffsetDateTime.now());
        product.setUnitOfMeasure(unitOfMeasure1);
        product.setCode(UUID.randomUUID().toString());
        try {
            Product savedProduct = productRepository.save(product);
            return Optional.of(ProductMapper.toDomain(savedProduct));
        } catch (DataIntegrityViolationException e) {
            String uuid = UUID.randomUUID().toString();
            throw new ServiceException(uuid, "Le produit " + product.getName() + " du tenant " + product.getTenantCode() + " existe deja");
        }
    }

    @Override
    public Optional<DomainProduct> findById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDomain);
    }

    @Override
    public Optional<DomainProduct> findByCode(String code) {
        Optional<Product> optionalProduct = this.productRepository.findByCode(code);
        return optionalProduct.map(ProductMapper::toDomain);

    }

    @Override
    public boolean isAllProductOfTenantCode(List<Long> productIds, String tenantCode) {
        return this.productRepository.isAllProductOfTenantCode(productIds, tenantCode, productIds.size());
    }

    @Override
    public Optional<List<DomainProduct>> findByIds(List<Long> productIds) {
        Optional<List<Product>> optionalProducts = this.productRepository.findByIds(productIds);
        return Optional.of(optionalProducts
                .orElse(Collections.emptyList())
                .stream()
                .map(ProductMapper::toDomain)
                .toList());

    }

    @Override
    public Optional<List<DomainProduct>> getAllProducts() {
        List<Product> products = this.productRepository.findAll();
        if (products.isEmpty()) {
            return Optional.empty();
        }
        List<DomainProduct> list = products.stream().map(ProductMapper::toLigthDomain).toList();
        return Optional.of(list);
    }

    @Override
    public Page<DomainProduct> getAllProductsByTenant(
            String tenantCode,
            int page,
            int size,
            String search               // ← ajouté
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> pageResult;
        if (search == null || search.trim().isEmpty()) {
            pageResult = productRepository.getAllProductOfTenantCode(tenantCode, pageable);
        } else {
            pageResult = productRepository
                    .findByTenantCodeAndNameContainingIgnoreCase(tenantCode, search, pageable);
        }

        return pageResult.map(product -> {
            DomainProduct dom = ProductMapper.toLigthDomain(product);
            articleRepository.findByProductId(product.getId())
                    .ifPresent(article ->
                            dom.setArticle(DomainArticle.builder()
                                    .id(article.getId())
                                    .quantity(article.getQuantity())
                                    .build()));
            return dom;
        });
    }

    @Override
    public Page<String> getAllUnitOfMeasurement(
            int page,
            int size,
            String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        if (search == null || search.trim().isEmpty()) {
            return unitOfMeasureRepository.findAllNames(pageable);
        }
        return unitOfMeasureRepository.findNamesBySearch(search, pageable);
    }
}
