package com.houssam.SmartLogi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "zone")
public class Zone {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Size(max = 10)
    @NotNull
    @Column(name = "code_postal", nullable = false, length = 10)
    private String codePostal;

    @OneToMany(mappedBy = "zone")
    private Set<Colis> colis = new LinkedHashSet<>();

    @OneToMany(mappedBy = "zoneAssignee")
    private Set<Livreur> livreurs = new LinkedHashSet<>();

    public Set<Livreur> getLivreurs() {
        return livreurs;
    }

    public void setLivreurs(Set<Livreur> livreurs) {
        this.livreurs = livreurs;
    }

    public Set<Colis> getColis() {
        return colis;
    }

    public void setColis(Set<Colis> colis) {
        this.colis = colis;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

