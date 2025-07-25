package com.bacos.mokengeli.biloko.infrastructure.repository;

import com.bacos.mokengeli.biloko.infrastructure.model.UnitOfMeasure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long> {

    Optional<UnitOfMeasure> findByName(String name);

    @Query("SELECT DISTINCT u.name FROM UnitOfMeasure u")
    Page<String> findAllNames(Pageable pageable);

    @Query(
            "SELECT DISTINCT u.name " +
                    "FROM UnitOfMeasure u " +
                    "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%'))"
    )
    Page<String> findNamesBySearch(
            @Param("search") String search,
            Pageable pageable
    );
}
