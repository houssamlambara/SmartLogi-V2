package com.houssam.SmartLogi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ZoneDTO {

    @NotNull(message = "Le nom de la zone est obligatoire")
    @Size(min = 2, max = 150)
    private String nom;

    @NotNull(message = "Le code postal est obligatoire")
    @Size(min = 3, max = 20)
    private String codePostal;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }
}
