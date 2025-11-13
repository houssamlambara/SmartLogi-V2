package com.houssam.SmartLogi.dto;

import com.houssam.SmartLogi.enums.Statut;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}
