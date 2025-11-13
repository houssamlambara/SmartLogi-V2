package com.houssam.SmartLogi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProduitDTO {

    private String id;

    @NotNull(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 100)
    private String nom;

    @NotNull(message = "La catégorie est obligatoire")
    @Size(min = 2, max = 100)
    private String categorie;

    @NotNull(message = "Le poids est obligatoire")
    @Positive(message = "Le poids doit être positif")
    private double poids;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private double prix;
}
