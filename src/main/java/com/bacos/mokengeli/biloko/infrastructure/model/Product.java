package com.bacos.mokengeli.biloko.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "name", nullable = false)
    private String name;  // Nom du produit (ex : "Bouteille d'eau minérale Volvic")

    @Column(name = "tenant_code", nullable = false)
    private String tenantCode;
    @Column(name = "description")
    private String description;  // Description de la catégorie

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;  // Catégorie du produit (ex : "Boisson")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_of_measure_id", nullable = false)
    private UnitOfMeasure unitOfMeasure;  // Unité de mesure (ex : "Litre", "Kg")

    @Column(name = "volume", nullable = false)
    private double volume;  // Volume par unité (ex : 1,5 L par bouteille)

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
