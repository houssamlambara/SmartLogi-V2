package com.houssam.SmartLogi.dto;

import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;

public class ColisDTO {

    private long id;
    private String description;
    private double poids;
    private Statut statut;
    private Prioriter priorite;
    private String villeDestination;

    private Long livreurId;
    private Long clientExpediteurId;
    private Long destinataireId;
    private Long zoneId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
