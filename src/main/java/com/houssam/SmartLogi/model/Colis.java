package com.houssam.SmartLogi.model;

import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "colis")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"livreur", "clientExpediteur", "destinataire", "zone", "historiqueLivraisons", "produits"})
public class Colis {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String description;
    private double poids;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Enumerated(EnumType.STRING)
    private Prioriter priorite;

    @Column(columnDefinition = "TEXT")
    private String villeDestination;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @ManyToOne
    @JoinColumn(name = "client_expediteur_id")
    private ClientExpediteur clientExpediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Destinataire destinataire;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @OneToMany(mappedBy = "colis")
    private List<HistoriqueLivraison> historiqueLivraisons;

    @OneToMany(mappedBy = "colis", fetch = FetchType.EAGER)
    private List<ColisProduit> produits;
}
