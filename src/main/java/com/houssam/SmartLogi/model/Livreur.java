package com.houssam.SmartLogi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "livreur")
public class Livreur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String telephone;
    private String vehicule;

    @Column(name = "zone_assignee")
    private String zoneAssignee;

    @OneToMany(mappedBy = "livreur")
    private List<Colis> colis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getZoneAssignee() {
        return zoneAssignee;
    }

    public void setZoneAssignee(String zoneAssignee) {
        this.zoneAssignee = zoneAssignee;
    }

    public List<Colis> getColis() {
        return colis;
    }

    public void setColis(List<Colis> colis) {
        this.colis = colis;
    }
}