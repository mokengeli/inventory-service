package com.bacos.mokengeli.biloko.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "stock_audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_movement_id", nullable = false)
    private StockMovement stockMovement;  // Mouvement de stock associé à cet audit

    @Column(name = "employee_number", nullable = false)
    private String employeeNumber; // Récupéré via le user-service

    @Column(name = "action_type", nullable = false)
    private String actionType;  // Type d'action (ex : "CREATE", "UPDATE", "DELETE")

    @Column(name = "description")
    private String description;  // Description de l'action effectuée

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;  // Date de l'audit
}

