package com.houssam.SmartLogi.dto;

import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public class ColisDTO {

    private String id;

    @NotNull(message = "La description est obligatoire")
    @Size(min = 2, max = 300, message = "La description doit contenir entre 2 et 300 caractères")
    private String description;

    @NotNull(message = "Le poids est obligatoire")
    @Positive(message = "Le poids doit être positif")
    private double poids;

    private Statut statut;

    @NotNull(message = "La priorité est obligatoire")
    private Prioriter priorite;

    @NotNull(message = "La ville de destination est obligatoire")
    @Size(min = 2, max = 50)
    private String villeDestination;

    // Livreur optionnel - sera assigné par le gestionnaire
    private String livreurId;

    @NotNull(message = "L'ID du client expéditeur est obligatoire")
    private String clientExpediteurId;

    @NotNull(message = "L'ID du destinataire est obligatoire")
    private String destinataireId;

    private String zoneId;

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Prioriter getPriorite() {
        return priorite;
    }

    public void setPriorite(Prioriter priorite) {
        this.priorite = priorite;
    }

    public String getVilleDestination() {
        return villeDestination;
    }

    public void setVilleDestination(String villeDestination) {
        this.villeDestination = villeDestination;
    }

    public String getLivreurId() {
        return livreurId;
    }

    public void setLivreurId(String livreurId) {
        this.livreurId = livreurId;
    }

    public String getClientExpediteurId() {
        return clientExpediteurId;
    }

    public void setClientExpediteurId(String clientExpediteurId) {
        this.clientExpediteurId = clientExpediteurId;
    }

    public String getDestinataireId() {
        return destinataireId;
    }

    public void setDestinataireId(String destinataireId) {
        this.destinataireId = destinataireId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

}

