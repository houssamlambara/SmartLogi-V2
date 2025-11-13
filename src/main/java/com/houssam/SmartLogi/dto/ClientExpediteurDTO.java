package com.houssam.SmartLogi.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientExpediteurDTO {

    @NotNull(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotNull(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;

    @NotNull(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotNull(message = "Le téléphone est obligatoire")
    @Size(min = 8, max = 20, message = "Le téléphone doit contenir entre 8 et 20 caractères")
    private String telephone;

    @NotNull(message = "L'adresse est obligatoire")
    @Size(min = 5, max = 100, message = "L'adresse doit contenir entre 5 et 100 caractères")
    private String adresse;
}
