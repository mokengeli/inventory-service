package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainProduct;

import java.util.List;
import java.util.Optional;

public interface ProductPort {

    DomainProduct addProduct(DomainProduct product);

    Optional<DomainProduct> findById(Long id);

    DomainProduct findByCode(String code);




}
