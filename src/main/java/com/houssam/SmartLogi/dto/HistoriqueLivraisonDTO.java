package com.houssam.SmartLogi.dto;

import com.houssam.SmartLogi.enums.Statut;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class HistoriqueLivraisonDTO {
    private String id;

    @NotNull(message = "L'ID du colis est obligatoire")
    private String colisId;

    @NotNull(message = "Le statut est obligatoire")
    private Statut statut;

    @NotNull(message = "La date de changement est obligatoire")
    private LocalDateTime dateChangement;

    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    private String commentaire;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColisId() {
        return colisId;
    }

    public void setColisId(String colisId) {
        this.colisId = colisId;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(LocalDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
