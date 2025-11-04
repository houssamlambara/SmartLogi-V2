package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ColisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colis")
@Tag(name = "Colis", description = "API pour gérer les colis")
public class ColisController {

    private final ColisService colisService;

    public ColisController (ColisService colisService) {
        this.colisService = colisService;
    }

    @PostMapping
    @Operation(summary = "Créer un colis", description = "Cette API permet de créer un nouveau colis")

    public ResponseEntity<ApiResponse<ColisDTO>> createColis(@Valid @RequestBody ColisDTO dto) {
        ColisDTO created = colisService.createColis(dto);
        return ResponseEntity.ok(new ApiResponse<>("Colis créé avec succès", created));
    }

    @GetMapping
    @Operation(summary = "Lister tous les colis", description = "Récupère tous les colis avec pagination")

    public ResponseEntity<ApiResponse<Page<ColisDTO>>> getAllColis(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ColisDTO> page = colisService.getAllColis(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des colis récupérée avec succès", page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un colis par ID", description = "Cherche un colis par son ID")

    public ResponseEntity<ApiResponse<ColisDTO>> getColisById(@PathVariable String id) {
        ColisDTO colis = colisService.getColisById(id);
        if (colis != null) {
            return ResponseEntity.ok(new ApiResponse<>("Colis trouvé", colis));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Colis non trouvé", null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un colis", description = "Supprime un colis par son ID")

    public ResponseEntity<ApiResponse<Void>> deleteColis(@PathVariable String id) {
        colisService.deleteColis(id);
        return ResponseEntity.ok(new ApiResponse<>("Colis supprimé avec succès", null));
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'un colis", description = "Change le statut d'un colis existant")

    public ResponseEntity<ApiResponse<ColisDTO>> updateStatut(
            @PathVariable String id,
            @RequestParam com.houssam.SmartLogi.enums.Statut nouveauStatut) {
        ColisDTO updated = colisService.updateStatut(id, nouveauStatut);
        return ResponseEntity.ok(new ApiResponse<>("Statut du colis mis à jour avec succès", updated));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrer les colis", description = "Filtre les colis selon différents critères")

    public ResponseEntity<ApiResponse<Page<ColisDTO>>> filterColis(
            @RequestParam(required = false) Statut statut,
            @RequestParam(required = false) String zoneId,
            @RequestParam(required = false) String villeDestination,
            @RequestParam(required = false) Prioriter priorite,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        // Appel du service pour filtrer les colis
        Page<ColisDTO> result = colisService.filterColis(statut, zoneId, villeDestination, priorite, pageable);

        return ResponseEntity.ok(new ApiResponse<>("Colis filtrés récupérés avec succès", result));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des colis", description = "Recherche des colis par mot-clé")

    public ResponseEntity<ApiResponse<Page<ColisDTO>>> searchColis(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ColisDTO> result = colisService.searchColis(keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(
                (keyword == null || keyword.isBlank())
                        ? "Tous les colis ont été récupérés"
                        : "Résultats de recherche pour : " + keyword,
                result
        ));
    }

    @GetMapping("/stats/by-zone")
    @Operation(summary = "Statistiques des colis par zone", description = "Récupère le nombre de colis par zone de livraison")
        public ResponseEntity<ApiResponse<Map<String, Long>>> getColisCountByZne(){
            Map<String, Long> stats = colisService.getColisCountByZone();
            return ResponseEntity.ok(new ApiResponse<>("Statistiques par zone", stats));
    }

    @GetMapping("/stats/by-statut")
    @Operation(summary = "Statistiques par statut", description = "Nombre de colis par statut")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getColisCountByStatut() {
        Map<String, Long> stats = colisService.getColisCountByStatut();
        return ResponseEntity.ok(new ApiResponse<>("Statistiques par statut", stats));
    }

    @GetMapping("/stats/by-priorite")
    @Operation(summary = "Statistiques des colis par priorité", description = "Récupère le nombre de colis par priorité")
        public ResponseEntity<ApiResponse<Map<String, Long>>> getColicCountByPriorite(){
        Map<String, Long> stats = colisService.getColisCountByPriorite();
        return ResponseEntity.ok(new ApiResponse<>("Statistiques par priorité", stats));
    }

    @GetMapping("/livreur/{livreurId}/stats")
    @Operation(summary = "Statistiques d'un livreur", description = "Nombre et poids total de colis d'un livreur")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatsLivreur(@PathVariable String livreurId){
        Map<String, Object> stats = colisService.getStatistiqueLivreur(livreurId);
        return ResponseEntity.ok(new ApiResponse<>("Statistiques du Livreur", stats));
    }

    @GetMapping("/zone/{zoneId}/stats")
    @Operation(summary = "Statistiques d'une zone", description = "Nombre et poids total de colis d'une zone")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatsZone(@PathVariable String zoneId){
        Map<String, Object> stats = colisService.getStatistiqueZone(zoneId);
        return ResponseEntity.ok(new ApiResponse<>("Statistiques de la Zone", stats));
    }
}