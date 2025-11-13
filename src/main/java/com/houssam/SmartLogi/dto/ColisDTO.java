package com.houssam.SmartLogi.dto;

import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
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

    private List<String> productIds;

    private List<ProduitDTO> nouveauxProduits;
}
