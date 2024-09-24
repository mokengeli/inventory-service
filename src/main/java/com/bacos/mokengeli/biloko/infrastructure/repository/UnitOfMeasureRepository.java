package com.bacos.mokengeli.biloko.infrastructure.repository;

import com.bacos.mokengeli.biloko.infrastructure.model.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitOfMeasureRepository  extends JpaRepository<UnitOfMeasure, Long> {

    Optional<UnitOfMeasure> findByName(String name);
}
