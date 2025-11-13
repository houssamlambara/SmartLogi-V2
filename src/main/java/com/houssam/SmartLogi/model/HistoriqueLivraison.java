package com.houssam.SmartLogi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_livraison")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"colis"})
public class HistoriqueLivraison {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String statut;
    private LocalDateTime dateChangement;
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "colis_id")
    private Colis colis;
}