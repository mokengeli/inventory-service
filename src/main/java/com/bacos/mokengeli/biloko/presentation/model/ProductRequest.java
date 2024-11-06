package com.bacos.mokengeli.biloko.presentation.model;

import lombok.Data;

@Data
public class ProductRequest {
    private Long productId;  // ID du produit existant
    private int numberOfUnits;  // Nombre d'articles à ajouter
}
