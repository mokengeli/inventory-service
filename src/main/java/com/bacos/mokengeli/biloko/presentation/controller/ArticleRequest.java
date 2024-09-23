package com.bacos.mokengeli.biloko.presentation.controller;

import lombok.Data;

@Data
public class ArticleRequest {
    private Long productId;  // ID du produit existant
    private int numberOfUnits;  // Nombre d'articles à ajouter
    private String employeeNumber;  // Numéro de l'employé qui effectue l'ajout
}
