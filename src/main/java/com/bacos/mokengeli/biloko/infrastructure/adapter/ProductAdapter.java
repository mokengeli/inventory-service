package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainArticle;
import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.domain.model.ConnectedUser;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;

import java.time.LocalDateTime;
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
        product.setCreatedAt(LocalDateTime.now());
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

    // Méthode mise à jour :
    @Override
    public Page<DomainProduct> getAllProductsByTenant(String tenantCode, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return  productRepository.getAllProductOfTenantCode(tenantCode, pageable)
                .map(product -> {
                    DomainProduct ligthDomain = ProductMapper.toLigthDomain(product);
                    Long id = product.getId();
                    Optional<Article> articleOpt = this.articleRepository.findByProductId(id);
                    if (articleOpt.isPresent()) {
                        Article article = articleOpt.get();
                        DomainArticle domainArticle = DomainArticle.builder()
                                .id(article.getId())
                                .quantity(article.getQuantity())
                                .build();
                        ligthDomain.setArticle(domainArticle);
                    }
                    return ligthDomain;
                });
    }

    @Override
    public Set<String> getAllUnitOfMeasurement() {
        List<UnitOfMeasure> all = this.unitOfMeasureRepository.findAll();
        if (all.isEmpty()) {
            return Collections.emptySet();
        }
        return all.stream().map(UnitOfMeasure::getName).collect(Collectors.toSet());
    }


}
