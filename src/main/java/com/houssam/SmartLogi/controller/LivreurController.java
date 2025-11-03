package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.service.ColisService;
import com.houssam.SmartLogi.service.LivreurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.houssam.SmartLogi.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/livreurs")
@Tag(name = "Livreurs", description = "API pour gérer les livreurs et leurs colis")

public class LivreurController {

    private final LivreurService livreurService;
    private final ColisService colisService;

    public LivreurController(LivreurService service, ColisService colisService) {
        this.livreurService = service;
        this.colisService = colisService;
    }

    @PostMapping
    @Operation(summary = "Créer un livreur", description = "Permet de créer un nouveau livreur")

    public ResponseEntity<ApiResponse<LivreurDTO>> createLivreur(@Valid @RequestBody LivreurDTO dto) {
        LivreurDTO created = livreurService.createLivreur(dto);
        return ResponseEntity.ok(new ApiResponse<>("Livreur créé avec succès", created));
    }

    @GetMapping
    @Operation(summary = "Lister tous les livreurs", description = "Récupère la liste paginée des livreurs")
    public ResponseEntity<ApiResponse<Page<LivreurDTO>>> getAllLivreurs(
            @PageableDefault(size = 20, sort = "nom") Pageable pageable) {
        Page<LivreurDTO> liste = livreurService.getAllLivreurs(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des livreurs récupérée avec succès", liste));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un livreur par ID", description = "Cherche un livreur par son ID")

    public ResponseEntity<ApiResponse<LivreurDTO>> getLivreurById(@PathVariable String id) {
        LivreurDTO livreur = livreurService.getLivreurById(id);
        if (livreur != null) {
            return ResponseEntity.ok(new ApiResponse<>("Livreur trouvé", livreur));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Livreur non trouvé", null));
        }
    }

    @GetMapping("/{id}/colis")
    @Operation(summary = "Récupérer les colis d'un livreur", description = "Liste tous les colis assignés à un livreur")

    public ResponseEntity<ApiResponse<Page<ColisDTO>>> getColisByLivreur(
            @PathVariable String id,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ColisDTO> colis = colisService.getColisByLivreurId(id, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des colis assignés au livreur récupérée avec succès", colis));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un livreur", description = "Modifie les informations d'un livreur existant")

    public ResponseEntity<ApiResponse<LivreurDTO>> updateLivreur(@PathVariable String id, @Valid @RequestBody LivreurDTO dto) {
        LivreurDTO updated = livreurService.updateLivreur(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Livreur mis à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un livreur", description = "Supprime un livreur par son ID")

    public ResponseEntity<ApiResponse<Void>> deleteLivreur(@PathVariable String id) {
        livreurService.deleteLivreur(id);
        return ResponseEntity.ok(new ApiResponse<>("Livreur supprimé avec succès", null));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des livreurs", description = "Recherche des livreurs par mot-clé (nom, prénom, téléphone, véhicule)")

    public ResponseEntity<ApiResponse<Page<LivreurDTO>>> searchLivreurs(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "nom", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<LivreurDTO> result = livreurService.searchLivreurs(keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(
                (keyword == null || keyword.isBlank())
                        ? "Tous les livreurs ont été récupérés"
                        : "Résultats de recherche pour : " + keyword,
                result
        ));
    }
}
