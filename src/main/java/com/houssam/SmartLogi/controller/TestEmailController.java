package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.email.EmailService;
import com.houssam.SmartLogi.enums.Statut;
import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.model.Livreur;
import com.houssam.SmartLogi.repository.ColisRepository;
import com.houssam.SmartLogi.repository.LivreurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-email")
public class TestEmailController {

    private final EmailService emailService;
    private final ColisRepository colisRepository;
    private final LivreurRepository livreurRepository;

    public TestEmailController(EmailService emailService, ColisRepository colisRepository, LivreurRepository livreurRepository) {
        this.emailService = emailService;
        this.colisRepository = colisRepository;
        this.livreurRepository = livreurRepository;
    }

    @PostMapping("/colis-cree/{idColis}")
    public ResponseEntity<String> testerEmailColisCreer(@PathVariable String idColis) {
        Colis colis = colisRepository.findById(idColis)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        String resultat = emailService.envoyerEmailColisCreer(colis);
        return ResponseEntity.ok(resultat);
    }

    @PostMapping("/colis-assigne/{idColis}")
    public ResponseEntity<String> testerEmailColisAssigne(@PathVariable String idColis) {
        Colis colis = colisRepository.findById(idColis)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        Livreur livreur = colis.getLivreur();
        if (livreur == null) {
            return ResponseEntity.badRequest().body("Le colis n'a pas de livreur assigné");
        }

        String resultat = emailService.envoyerEmailColisAssigne(colis, livreur);
        return ResponseEntity.ok(resultat);
    }

    @PostMapping("/colis-statut/{idColis}")
    public ResponseEntity<String> testerEmailStatutColis(
            @PathVariable String idColis,
            @RequestParam Statut ancienStatut,
            @RequestParam Statut nouveauStatut) {

        Colis colis = colisRepository.findById(idColis)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        Livreur livreur = colis.getLivreur();
        if (livreur == null) {
            return ResponseEntity.badRequest().body("Le colis n'a pas de livreur assigné");
        }

        String resultat = emailService.envoyerEmailStatutMisAJour(colis, livreur, ancienStatut, nouveauStatut);
        return ResponseEntity.ok(resultat);
    }
}

