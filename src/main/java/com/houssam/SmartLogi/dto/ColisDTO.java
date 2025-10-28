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

}
