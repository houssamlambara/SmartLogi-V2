package com.houssam.SmartLogi.email;

import com.houssam.SmartLogi.enums.Statut;
import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.model.Livreur;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${mail.username}")
    private String emailExpediteur;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    // Envoyer un email simple (conservé pour compatibilité)
    public void envoyerEmail(String destinataire, String sujet, String contenu) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject(sujet);
        message.setText(contenu);
        message.setFrom(emailExpediteur);

        mailSender.send(message);
    }

    // Envoyer à plusieurs destinataires (conservé pour compatibilité)
    public void envoyerEmailMultiple(String[] destinataires, String sujet, String contenu) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataires);
        message.setSubject(sujet);
        message.setText(contenu);
        message.setFrom(emailExpediteur);

        mailSender.send(message);
    }

    public String envoyerEmailColisCreer(Colis colis) {
        try {
            Context ctx = new Context();

            // Informations expéditeur
            ctx.setVariable("nomExpediteur", colis.getClientExpediteur().getNom() + " " + colis.getClientExpediteur().getPrenom());

            // Date et heure de création
            ctx.setVariable("dateCreation", LocalDate.now());
            ctx.setVariable("heureCreation", colis.getCreatedAt() != null ?
                colis.getCreatedAt().toLocalTime().toString() : java.time.LocalDateTime.now().toLocalTime().toString());

            // Informations du colis
            ctx.setVariable("idColis", colis.getId());
            ctx.setVariable("description", colis.getDescription() != null ? colis.getDescription() : "N/A");
            ctx.setVariable("statut", colis.getStatut().toString());
            ctx.setVariable("priorite", colis.getPriorite().toString());
            ctx.setVariable("poids", colis.getPoids());
            ctx.setVariable("destination", colis.getVilleDestination());
            ctx.setVariable("zone", colis.getZone() != null ? colis.getZone().getNom() : "Non définie");

            // Informations destinataire
            ctx.setVariable("nomDestinataire", colis.getDestinataire().getNom() + " " + colis.getDestinataire().getPrenom());
            ctx.setVariable("emailDestinataire", colis.getDestinataire().getEmail() != null ? colis.getDestinataire().getEmail() : "Non renseigné");
            ctx.setVariable("telephoneDestinataire", colis.getDestinataire().getTelephone() != null ? colis.getDestinataire().getTelephone() : "Non renseigné");
            ctx.setVariable("adresseDestinataire", colis.getDestinataire().getAdresse() != null ? colis.getDestinataire().getAdresse() : "Non renseignée");

            // Informations livreur
            ctx.setVariable("nomLivreur", colis.getLivreur() != null ?
                colis.getLivreur().getNom() + " " + colis.getLivreur().getPrenom() : "Non assigné");
            ctx.setVariable("telephoneLivreur", colis.getLivreur() != null && colis.getLivreur().getTelephone() != null ?
                colis.getLivreur().getTelephone() : "Non renseigné");

            // Liste des produits
            if (colis.getProduits() != null && !colis.getProduits().isEmpty()) {
                java.util.List<java.util.Map<String, Object>> produitsInfo = new java.util.ArrayList<>();
                for (var colisProduit : colis.getProduits()) {
                    java.util.Map<String, Object> produitMap = new java.util.HashMap<>();
                    produitMap.put("nom", colisProduit.getProduit().getNom());
                    produitMap.put("categorie", colisProduit.getProduit().getCategorie() != null ?
                        colisProduit.getProduit().getCategorie() : "Non catégorisé");
                    produitMap.put("prix", colisProduit.getProduit().getPrix());
                    produitMap.put("poids", colisProduit.getProduit().getPoids());
                    produitsInfo.add(produitMap);
                }
                ctx.setVariable("produits", produitsInfo);
                ctx.setVariable("nombreProduits", produitsInfo.size());
            } else {
                ctx.setVariable("produits", new java.util.ArrayList<>());
                ctx.setVariable("nombreProduits", 0);
            }

            String htmlBody = templateEngine.process("email/colis-created", ctx);

            return envoyerEmailHtml(colis.getClientExpediteur().getEmail(),
                    "📦 Votre Colis a été Créé avec Succès — [ID de suivi: " + colis.getId() + "]",
                    htmlBody);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'envoi de l'email de création.";
        }
    }

    public String envoyerEmailColisAssigne(Colis colis, Livreur livreur) {
        try {
            Context ctx = new Context();
            ctx.setVariable("nomExpediteur", colis.getClientExpediteur().getNom() + " " + colis.getClientExpediteur().getPrenom());
            ctx.setVariable("idColis", colis.getId());
            ctx.setVariable("destination", colis.getVilleDestination());
            ctx.setVariable("nomDestinataire", colis.getDestinataire().getNom() + " " + colis.getDestinataire().getPrenom());
            ctx.setVariable("statut", colis.getStatut().toString());
            ctx.setVariable("nomLivreur", livreur.getNom() + " " + livreur.getPrenom());
            ctx.setVariable("villeLivreur", livreur.getZoneAssignee().getNom());
            ctx.setVariable("telephoneLivreur", livreur.getTelephone());

            String htmlBody = templateEngine.process("email/colis-assigned", ctx);

            return envoyerEmailHtml(colis.getClientExpediteur().getEmail(),
                    "🚚 Votre Colis a été Assigné à un Livreur — [ID de suivi: " + colis.getId() + "]",
                    htmlBody);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'envoi de l'email d'assignation.";
        }
    }

    public String envoyerEmailStatutMisAJour(Colis colis, Livreur livreur, Statut ancienStatut, Statut nouveauStatut) {
        try {
            Context ctx = new Context();
            ctx.setVariable("nomExpediteur", colis.getClientExpediteur().getNom() + " " + colis.getClientExpediteur().getPrenom());
            ctx.setVariable("idColis", colis.getId());
            ctx.setVariable("nomLivreur", livreur.getNom() + " " + livreur.getPrenom());
            ctx.setVariable("ancienStatut", ancienStatut);
            ctx.setVariable("nouveauStatut", nouveauStatut);

            String htmlBody = templateEngine.process("email/colis-status", ctx);

            return envoyerEmailHtml(colis.getClientExpediteur().getEmail(),
                    "🔄 Statut du Colis Mis à Jour — [ID de suivi: " + colis.getId() + "]",
                    htmlBody);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'envoi de l'email de mise à jour du statut.";
        }
    }

    private String envoyerEmailHtml(String destinataire, String sujet, String corpsHtml) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(emailExpediteur);
        helper.setTo(destinataire);
        helper.setSubject(sujet);
        helper.setText(corpsHtml, true);

        mailSender.send(message);
        return "Email envoyé avec succès.";
    }
}

