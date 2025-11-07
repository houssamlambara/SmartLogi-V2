package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.email.EmailService;
import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import com.houssam.SmartLogi.exception.ResourceNotFoundException;
import com.houssam.SmartLogi.mapper.ColisMapper;
import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.model.ColisProduit;
import com.houssam.SmartLogi.model.Produit;
import com.houssam.SmartLogi.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ColisService {

    private final ColisRepository colisRepository;
    private final ColisMapper colisMapper;
    private final LivreurRepository livreurRepository;
    private final ClientExpediteurRepository clientRepository;
    private final DestinataireRepository destinataireRepository;
    private final ZoneRepository zoneRepository;
    private final ProduitRepository produitRepository;
    private final ColisProduitRepository colisProduitRepository;
    private final EmailService emailService;

    public ColisService(ColisRepository colisRepository, ColisMapper colisMapper,
                        LivreurRepository livreurRepository,
                        ClientExpediteurRepository clientRepository,
                        DestinataireRepository destinataireRepository,
                        ZoneRepository zoneRepository,
                        ProduitRepository produitRepository,
                        ColisProduitRepository colisProduitRepository,
                        EmailService emailService) {
        this.colisRepository = colisRepository;
        this.colisMapper = colisMapper;
        this.livreurRepository = livreurRepository;
        this.clientRepository = clientRepository;
        this.destinataireRepository = destinataireRepository;
        this.zoneRepository = zoneRepository;
        this.produitRepository = produitRepository;
        this.colisProduitRepository = colisProduitRepository;
        this.emailService = emailService;
    }

    @Transactional
    public ColisDTO createColis(ColisDTO dto) {
        Colis colis = colisMapper.toEntity(dto);
        validerEntites(colis, dto);
        initialiserValeursParDefaut(colis);

        Colis saved = colisRepository.save(colis);

        // 5. Traitement des produits (méthodes extraites)
        List<ColisProduit> colisProduits = new ArrayList<>();
        colisProduits.addAll(traiterProduitsExistants(saved, dto.getProductIds()));
        colisProduits.addAll(traiterNouveauxProduits(saved, dto.getNouveauxProduits()));

        if (!colisProduits.isEmpty()) {
            sauvegarderProduits(colisProduits);
            saved.setProduits(colisProduits);
        }
        try {
            if (saved.getClientExpediteur() != null &&
                    saved.getClientExpediteur().getEmail() != null) {
                emailService.envoyerEmailColisCreer(saved);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Erreur envoi email : " + e.getMessage());
        }

        ColisDTO resultDTO = colisMapper.toDTO(saved);

        // Remplir manuellement les productIds et nouveauxProduits
        if (!colisProduits.isEmpty()) {
            List<String> idsProduits = colisProduits.stream()
                    .map(cp -> cp.getProduit().getId())
                    .collect(Collectors.toList());
            resultDTO.setProductIds(idsProduits);

            List<ProduitDTO> tousProduitsDTO = colisProduits.stream()
                    .map(cp -> {
                        Produit p = cp.getProduit();
                        ProduitDTO pdto = new ProduitDTO();
                        pdto.setNom(p.getNom());
                        pdto.setCategorie(p.getCategorie());
                        pdto.setPrix(p.getPrix());
                        pdto.setPoids(p.getPoids());
                        return pdto;
                    }).collect(Collectors.toList());

            resultDTO.setNouveauxProduits(tousProduitsDTO);
        }

        return resultDTO;
    }

    private void validerEntites(Colis colis, ColisDTO dto) {
        colis.setLivreur(livreurRepository.findById(dto.getLivreurId())
                        .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable avec l'ID " + dto.getLivreurId())));

        colis.setClientExpediteur(clientRepository.findById(dto.getClientExpediteurId())
                        .orElseThrow(() -> new ResourceNotFoundException("ClientExpediteur introuvable avec l'ID " + dto.getClientExpediteurId())));

        colis.setDestinataire(destinataireRepository.findById(dto.getDestinataireId())
                        .orElseThrow(() -> new ResourceNotFoundException("Destinataire introuvable avec l'ID " + dto.getDestinataireId())));

        colis.setZone(zoneRepository.findById(dto.getZoneId())
                        .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID " + dto.getZoneId())));
    }

    private void initialiserValeursParDefaut(Colis colis) {
        if (colis.getStatut() == null) {
            colis.setStatut(Statut.Creer);
        }
        if (colis.getCreatedAt() == null) {
            colis.setCreatedAt(LocalDateTime.now());
        }
    }

    private List<ColisProduit> traiterProduitsExistants(Colis colis, List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Produit> produits = produitRepository.findAllById(productIds);

        if (produits.size() != productIds.size()) {
            throw new ResourceNotFoundException("Certains produits n'existent pas avec les IDs fournis");}

        return produits.stream()
                .map(produit -> creerColisProduit(colis, produit))
                .collect(Collectors.toList());
    }

    private List<ColisProduit> traiterNouveauxProduits(Colis colis, List<ProduitDTO> nouveauxProduits) {
        if (nouveauxProduits == null || nouveauxProduits.isEmpty()) {
            return new ArrayList<>();
        }

        return nouveauxProduits.stream()
                .map(pdto -> {
                    // Créer le nouveau produit
                    Produit produit = new Produit();
                    produit.setNom(pdto.getNom());
                    produit.setCategorie(pdto.getCategorie());
                    produit.setPrix(pdto.getPrix());
                    produit.setPoids(pdto.getPoids());

                    // Créer la relation ColisProduit
                    return creerColisProduit(colis, produit);
                })
                .collect(Collectors.toList());
    }

    private ColisProduit creerColisProduit(Colis colis, Produit produit) {
        ColisProduit cp = new ColisProduit();
        cp.setColis(colis);
        cp.setProduit(produit);
        cp.setQuantite(1);
        cp.setPrix(produit.getPrix());
        cp.setDateAjout(LocalDateTime.now());
        return cp;
    }

    private void sauvegarderProduits(List<ColisProduit> colisProduits) {

        List<Produit> nouveauxProduits = colisProduits.stream()
                .map(ColisProduit::getProduit)
                .filter(p -> p.getId() == null)
                .collect(Collectors.toList());

        if (!nouveauxProduits.isEmpty()) {
            produitRepository.saveAll(nouveauxProduits);
        }

        colisProduitRepository.saveAll(colisProduits);
    }

    private double calculerPoidsTotalProduits(List<ColisProduit> colisProduits) {
        return colisProduits.stream()
                .mapToDouble(cp -> cp.getProduit().getPoids() * cp.getQuantite())
                .sum();
    }

//    public ColisDTO createColis(ColisDTO dto) {
//        Colis colis = colisMapper.toEntity(dto);
//
//        colis.setLivreur(livreurRepository.findById(dto.getLivreurId())
//                .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable avec l'ID " + dto.getLivreurId())));
//
//        colis.setClientExpediteur(clientRepository.findById(dto.getClientExpediteurId())
//                .orElseThrow(() -> new ResourceNotFoundException("ClientExpediteur introuvable avec l'ID " + dto.getClientExpediteurId())));
//
//        colis.setDestinataire(destinataireRepository.findById(dto.getDestinataireId())
//                .orElseThrow(() -> new ResourceNotFoundException("Destinataire introuvable avec l'ID " + dto.getDestinataireId())));
//
//        colis.setZone(zoneRepository.findById(dto.getZoneId())
//                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID " + dto.getZoneId())));
//
//        Colis saved = colisRepository.save(colis);
//
//        List<ColisProduit> colisProduits = new ArrayList<>();
//
//        if (dto.getProductIds() != null && !dto.getProductIds().isEmpty()) {
//            List<Produit> produits = produitRepository.findAllById(dto.getProductIds());
//            if (produits.size() != dto.getProductIds().size()) {
//                throw new ResourceNotFoundException("Certains produits n'existent pas avec les IDs fournis");
//            }
//
//            for (Produit produit : produits) {
//                ColisProduit cp = new ColisProduit();
//                cp.setColis(saved);
//                cp.setProduit(produit);
//                cp.setQuantite(1);
//                cp.setPrix(produit.getPrix());
//                cp.setDateAjout(LocalDateTime.now());
//                colisProduitRepository.save(cp);
//                colisProduits.add(cp);
//            }
//        }
//
//        if (dto.getNouveauxProduits() != null && !dto.getNouveauxProduits().isEmpty()) {
//            for (ProduitDTO pdto : dto.getNouveauxProduits()) {
//                Produit produit = new Produit();
//                produit.setNom(pdto.getNom());
//                produit.setCategorie(pdto.getCategorie());
//                produit.setPrix(pdto.getPrix());
//                produit.setPoids(pdto.getPoids());
//                produitRepository.save(produit);
//
//                ColisProduit cp = new ColisProduit();
//                cp.setColis(saved);
//                cp.setProduit(produit);
//                cp.setQuantite(1);
//                cp.setPrix(produit.getPrix());
//                cp.setDateAjout(LocalDateTime.now());
//                colisProduitRepository.save(cp);
//                colisProduits.add(cp);
//            }
//        }
//
//        saved.setProduits(colisProduits);
//
//        ColisDTO resultDTO = colisMapper.toDTO(saved);
//
//        List<String> idsProduits = colisProduits.stream()
//                .map(cp -> cp.getProduit().getId())
//                .collect(Collectors.toList());
//        resultDTO.setProductIds(idsProduits);
//
//        List<ProduitDTO> tousProduitsDTO = colisProduits.stream()
//                .map(cp -> {
//                    Produit p = cp.getProduit();
//                    ProduitDTO pdto = new ProduitDTO();
//                    pdto.setNom(p.getNom());
//                    pdto.setCategorie(p.getCategorie());
//                    pdto.setPrix(p.getPrix());
//                    pdto.setPoids(p.getPoids());
//                    return pdto;
//                }).collect(Collectors.toList());
//
//        resultDTO.setNouveauxProduits(tousProduitsDTO);
//
//        return resultDTO;
//    }

    public Page<ColisDTO> getAllColis(Pageable pageable) {
        return colisRepository.findAll(pageable)
                .map(colisMapper::toDTO);
    }

    public Page<ColisDTO> getColisByDestinataireId(String destinataireId, Pageable pageable) {
        // 1. Vérifier que le destinataire existe
        destinataireRepository.findById(destinataireId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destinataire introuvable avec l'ID " + destinataireId));

        // 2. Récupérer et mapper les colis avec pagination
        return colisRepository.findByDestinataireId(destinataireId, pageable)
                .map(colisMapper::toDTO);
    }

    public Page<ColisDTO> getColisByLivreurId(String livreurId, Pageable pageable) {
        // 1. Vérifier que le livreur existe
        livreurRepository.findById(livreurId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livreur introuvable avec l'ID " + livreurId));

        // 2. Récupérer et mapper les colis assignés à ce livreur avec pagination
        return colisRepository.findByLivreurId(livreurId, pageable)
                .map(colisMapper::toDTO);
    }

    public ColisDTO getColisById(String id) {
        return colisRepository.findById(id)
                .map(colisMapper::toDTO)
                .orElse(null);
    }

    public ColisDTO updateStatut(String colisId, Statut nouveauStatut) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new ResourceNotFoundException("Colis introuvable avec l'ID " + colisId));
        colis.setStatut(nouveauStatut);
        Colis updated = colisRepository.save(colis);
        return colisMapper.toDTO(updated);
    }

    public void deleteColis(String id) {
        colisRepository.deleteById(id);
    }

    public Page<ColisDTO> filterColis(
            Statut statut,
            String zoneId,
            String villeDestination,
            Prioriter priorite,
            Pageable pageable
    ) {
        return colisRepository.filterColis(statut, zoneId, villeDestination, priorite, pageable)
                .map(colisMapper::toDTO);
    }

    public Page<ColisDTO> searchColis(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return colisRepository.findAll(pageable).map(colisMapper::toDTO);
        }
        return colisRepository.searchColis(keyword.trim(), pageable)
                .map(colisMapper::toDTO);
    }

    public Map<String , Long> getColisCountByZone(){
        List<Colis> allColis = colisRepository.findAll();
        return allColis.stream()
                .collect(Collectors.groupingBy(
                c -> c.getZone() != null ? c.getZone().getNom() : "Sans Zone",
                        Collectors.counting()
                ));
    }

    public Map<String , Long> getColisCountByStatut(){
        List<Colis> allColis = colisRepository.findAll();
        return allColis.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getStatut().name(),
                        Collectors.counting()
                ));
    }

    public Map<String, Long> getColisCountByPriorite(){
        List<Colis> allColis = colisRepository.findAll();
        return allColis.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getPriorite().name(),
                        Collectors.counting()
                ));
    }

    public Map<String, Object> getStatistiqueLivreur(String LivreurId){
        List<Colis> colis = colisRepository.findByLivreurId(LivreurId);

        Map<String , Object> stats = new HashMap<>();
        stats.put("LivreurId", LivreurId);
        stats.put("NombreColis", colis.size());
        stats.put("PoidsTotal",colis.stream().mapToDouble(Colis::getPoids).sum());

        return stats;
    }

    public Map<String, Object> getStatistiqueZone(String zoneId){
        List<Colis> colis = colisRepository.findByZoneId(zoneId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("ZoneId", zoneId);
        stats.put("NombreColis", colis.size());
        stats.put("PoidsTotal",colis.stream().mapToDouble(Colis::getPoids).sum());

        return stats;
    }

}
