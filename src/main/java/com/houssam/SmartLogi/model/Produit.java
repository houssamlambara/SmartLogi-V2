package com.houssam.SmartLogi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String categorie;
    private Double poids;
    private Double prix;

    @OneToMany(mappedBy = "produit")
    private List<ColisProduit> colisProduits;

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

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public List<ColisProduit> getColisProduits() {
        return colisProduits;
    }

    public void setColisProduits(List<ColisProduit> colisProduits) {
        this.colisProduits = colisProduits;
    }
}
