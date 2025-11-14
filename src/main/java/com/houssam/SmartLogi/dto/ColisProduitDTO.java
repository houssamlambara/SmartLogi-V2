package com.houssam.SmartLogi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ColisProduitDTO {

    @NotNull(message = "L'ID du colis est obligatoire")
    private String colisId;

    @NotNull(message = "L'ID du produit est obligatoire")
    private String produitId;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private int quantite;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private double prix;

    private LocalDate dateAjout;
}
