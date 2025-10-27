package com.houssam.SmartLogi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "colis")
public class Colis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private double poids;
    private String statut;
    private String priorite;
    private String villeDestination;

    @ManyToOne
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @ManyToOne
    @JoinColumn(name = "client_expediteur_id")
    private ClientExpediteur clientExpediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Destinataire destinataire;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @OneToMany(mappedBy = "colis")
    private List<HistoriqueLivraison> historiqueLivraisons;

    @OneToMany(mappedBy = "colis")
    private List<ColisProduit> produits;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getVilleDestination() {
        return villeDestination;
    }

    public void setVilleDestination(String villeDestination) {
        this.villeDestination = villeDestination;
    }

    public Livreur getLivreur() {
        return livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }

    public ClientExpediteur getClientExpediteur() {
        return clientExpediteur;
    }

    public void setClientExpediteur(ClientExpediteur clientExpediteur) {
        this.clientExpediteur = clientExpediteur;
    }

    public Destinataire getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(Destinataire destinataire) {
        this.destinataire = destinataire;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public List<HistoriqueLivraison> getHistoriqueLivraisons() {
        return historiqueLivraisons;
    }

    public void setHistoriqueLivraisons(List<HistoriqueLivraison> historiqueLivraisons) {
        this.historiqueLivraisons = historiqueLivraisons;
    }

    public List<ColisProduit> getProduits() {
        return produits;
    }

    public void setProduits(List<ColisProduit> produits) {
        this.produits = produits;
    }
}
