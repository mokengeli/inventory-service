package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;
import com.bacos.mokengeli.biloko.application.port.ProductPort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.ProductMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.Product;
import com.bacos.mokengeli.biloko.infrastructure.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductAdapter implements ProductPort {

    private final ProductRepository productRepository;

    @Autowired
    public ProductAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public DomainProduct save(DomainProduct domainProduct) {
        Product product = ProductMapper.toEntity(domainProduct);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDomain(savedProduct);
    }

    @Override
    public Optional<DomainProduct> findById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDomain);
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
