package com.houssam.SmartLogi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LivreurDTO {

    @NotNull(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50)
    private String nom;

    @NotNull(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50)
    private String prenom;

    @Email
    private String email;

    @NotNull(message = "Le mot de passe est obligatoire")
    @Size(min = 3, max = 20)
    private String password;

    @NotNull(message = "Le téléphone est obligatoire")
    @Size(min = 8, max = 20)
    private String telephone;

    @NotNull(message = "Le véhicule est obligatoire")
    @Size(min = 2, max = 50)
    private String vehicule;

    @NotNull(message = "L'ID de la zone assignée est obligatoire")
    private String zoneAssigneeId;
}
