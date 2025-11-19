package com.houssam.SmartLogi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 3)
    private String password;

    @NotBlank
    private String telephone;

    @NotBlank
    private String adresse;
}
