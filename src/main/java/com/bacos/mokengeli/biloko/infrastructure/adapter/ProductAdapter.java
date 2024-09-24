package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.ProductMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.Product;
import com.bacos.mokengeli.biloko.infrastructure.model.UnitOfMeasure;
import com.bacos.mokengeli.biloko.infrastructure.repository.ProductRepository;
import com.bacos.mokengeli.biloko.infrastructure.repository.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductAdapter implements ProductPort {

    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    public ProductAdapter(ProductRepository productRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.productRepository = productRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public DomainProduct addProduct(DomainProduct domainProduct) {
        Product product = ProductMapper.toEntity(domainProduct);
        String unitOfMeasure = domainProduct.getUnitOfMeasure();
        UnitOfMeasure unitOfMeasure1 = this.unitOfMeasureRepository.findByName(unitOfMeasure)
                .orElseThrow(() -> new RuntimeException("No unit of measure found with name: " + unitOfMeasure));
        product.setCreatedAt(LocalDateTime.now());
        product.setUnitOfMeasure(unitOfMeasure1);
        product.setCode(UUID.randomUUID().toString());
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDomain(savedProduct);
    }

    @Override
    public Optional<DomainProduct> findById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDomain);
    }

    @Override
    public DomainProduct findByCode(String code) {
        Product product = this.productRepository.findByCode(code).orElseThrow(() -> new RuntimeException("No product found with code: " + code));
        return ProductMapper.toDomain(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<DomainProduct> findAllByTenantCode(String tenantCode) {
        return productRepository.findAllByTenantCode(tenantCode)
                .stream()
                .map(ProductMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DomainProduct> getProductByIdAndTenantId(Long productId, Long tenantId) {
        return Optional.empty();
    }
}
