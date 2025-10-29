package com.houssam.SmartLogi.dto;

import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ColisDTO {

    @NotNull(message = "La description est obligatoire")
    @Size(min = 2, max = 300, message = "La description doit contenir entre 2 et 300 caractères")
    private String description;

    @NotNull(message = "Le poids est obligatoire")
    @Positive(message = "Le poids doit être positif")
    private double poids;

    @NotNull(message = "Le statut est obligatoire")
    private Statut statut;

    @NotNull(message = "La priorité est obligatoire")
    private Prioriter priorite;

    @NotNull(message = "La ville de destination est obligatoire")
    @Size(min = 2, max = 50)
    private String villeDestination;

    @NotNull(message = "L'ID du livreur est obligatoire")
    private Long livreurId;

    @NotNull(message = "L'ID du client expéditeur est obligatoire")
    private Long clientExpediteurId;

    @NotNull(message = "L'ID du destinataire est obligatoire")
    private Long destinataireId;

    @NotNull(message = "L'ID de la zone est obligatoire")
    private Long zoneId;


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

    public Long getLivreurId() {
        return livreurId;
    }

    public void setLivreurId(Long livreurId) {
        this.livreurId = livreurId;
    }

    public Long getClientExpediteurId() {
        return clientExpediteurId;
    }

    public void setClientExpediteurId(Long clientExpediteurId) {
        this.clientExpediteurId = clientExpediteurId;
    }

    public Long getDestinataireId() {
        return destinataireId;
    }

    public void setDestinataireId(Long destinataireId) {
        this.destinataireId = destinataireId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }
}
