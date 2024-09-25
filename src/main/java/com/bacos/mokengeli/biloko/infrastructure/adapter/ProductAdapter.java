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
import com.bacos.mokengeli.biloko.application.exception.ServiceException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
        } catch (Exception e) {
            throw new ServiceException(UUID.randomUUID().toString(), e.getMessage());
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


}
