package com.houssam.SmartLogi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "colis_produit")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"colis", "produit"})
public class ColisProduit {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private Integer quantite;
    private Double prix;
    private LocalDateTime dateAjout;

    @ManyToOne
    @JoinColumn(name = "colis_id")
    private Colis colis;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;
}
