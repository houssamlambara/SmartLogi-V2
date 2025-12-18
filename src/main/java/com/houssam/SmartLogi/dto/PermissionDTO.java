package com.houssam.SmartLogi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionDTO {

    private String id;

    @NotBlank(message = "Le nom de la permission est obligatoire")
    private String name;

    private String description;
}

