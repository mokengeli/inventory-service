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
    @Column(name = "employee_number", nullable = false)
    private String employeeNumber;
    @Column(name = "observation")
    private String observation;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;  // Article concerné par le mouvement de stock

    @Column(name = "movement_type", nullable = false)
    private String movementType;  // Type de mouvement : "ENTREE" ou "SORTIE"

    @Column(name = "old_quantity", nullable = false)
    private double oldQuantity;

    @Column(name = "quantity_moved", nullable = false)
    private double quantityMoved;  // Quantité déplacée (ex : 3 litres pour une sortie de stock)

    @Column(name = "new_quantity", nullable = false)
    private double newQuantity;

    @Column(name = "unit_of_measure", nullable = false)
    private String unitOfMeasure;

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;  // Date du mouvement

}
