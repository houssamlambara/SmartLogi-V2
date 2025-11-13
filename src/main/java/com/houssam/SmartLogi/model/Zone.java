package com.houssam.SmartLogi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "zone")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"colis", "livreurs"})
public class Zone {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Size(max = 10)
    @NotNull
    @Column(name = "code_postal", nullable = false, length = 10)
    private String codePostal;

    @OneToMany(mappedBy = "zone")
    private Set<Colis> colis = new LinkedHashSet<>();

    @OneToMany(mappedBy = "zoneAssignee")
    private Set<Livreur> livreurs = new LinkedHashSet<>();
}

