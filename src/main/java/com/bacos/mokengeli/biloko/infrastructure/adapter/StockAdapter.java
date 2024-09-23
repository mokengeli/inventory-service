package com.bacos.mokengeli.biloko.infrastructure.adapter;

import com.bacos.mokengeli.biloko.application.domain.DomainStockMovement;
import com.bacos.mokengeli.biloko.application.port.StockPort;
import com.bacos.mokengeli.biloko.infrastructure.mapper.StockMapper;
import com.bacos.mokengeli.biloko.infrastructure.model.StockMovement;
import com.bacos.mokengeli.biloko.infrastructure.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StockAdapter implements StockPort {

    private final StockRepository stockRepository;

    @Autowired
    public StockAdapter(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public DomainStockMovement save(DomainStockMovement domainStockMovement) {
        StockMovement stockMovement = StockMapper.toEntity(domainStockMovement);
        StockMovement savedStockMovement = stockRepository.save(stockMovement);
        return StockMapper.toDomain(savedStockMovement);
    }

    @Override
    public Optional<DomainStockMovement> findById(Long id) {
        return stockRepository.findById(id)
                .map(StockMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public List<DomainStockMovement> findAllByTenantId(Long tenantId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
