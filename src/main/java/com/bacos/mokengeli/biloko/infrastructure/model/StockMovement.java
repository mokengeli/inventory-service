package com.bacos.mokengeli.biloko.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;  // Article concerné par le mouvement de stock

    @Column(name = "movement_type", nullable = false)
    private String movementType;  // Type de mouvement : "ENTREE" ou "SORTIE"

    @Column(name = "quantity_moved", nullable = false)
    private double quantityMoved;  // Quantité déplacée (ex : 3 litres pour une sortie de stock)

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;  // Date du mouvement

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
