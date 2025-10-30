package com.houssam.SmartLogi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LivreurDTO {

    @NotNull(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50)
    private String nom;

    @NotNull(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50)
    private String prenom;

    @NotNull(message = "Le téléphone est obligatoire")
    @Size(min = 8, max = 20)
    private String telephone;

    @NotNull(message = "Le véhicule est obligatoire")
    @Size(min = 2, max = 50)
    private String vehicule;

    @NotNull(message = "L'ID de la zone assignée est obligatoire")
    private Long zoneAssigneeId;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public Long getZoneAssigneeId() {
        return zoneAssigneeId;
    }

    public void setZoneAssigneeId(Long zoneAssigneeId) {
        this.zoneAssigneeId = zoneAssigneeId;
    }
}
