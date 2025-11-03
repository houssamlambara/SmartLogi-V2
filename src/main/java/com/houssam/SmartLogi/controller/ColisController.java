package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ColisService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colis")
public class ColisController {

    private final ColisService colisService;

    public ColisController (ColisService colisService) {
        this.colisService = colisService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ColisDTO>> createColis(@Valid @RequestBody ColisDTO dto) {
        ColisDTO created = colisService.createColis(dto);
        return ResponseEntity.ok(new ApiResponse<>("Colis créé avec succès", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ColisDTO>>> getAllColis(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ColisDTO> page = colisService.getAllColis(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des colis récupérée avec succès", page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ColisDTO>> getColisById(@PathVariable String id) {
        ColisDTO colis = colisService.getColisById(id);
        if (colis != null) {
            return ResponseEntity.ok(new ApiResponse<>("Colis trouvé", colis));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Colis non trouvé", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteColis(@PathVariable String id) {
        colisService.deleteColis(id);
        return ResponseEntity.ok(new ApiResponse<>("Colis supprimé avec succès", null));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<ApiResponse<ColisDTO>> updateStatut(
            @PathVariable String id,
            @RequestParam com.houssam.SmartLogi.enums.Statut nouveauStatut) {
        ColisDTO updated = colisService.updateStatut(id, nouveauStatut);
        return ResponseEntity.ok(new ApiResponse<>("Statut du colis mis à jour avec succès", updated));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<ColisDTO>>> filterColis(
            @RequestParam(required = false) Statut statut,
            @RequestParam(required = false) String zoneId,
            @RequestParam(required = false) String villeDestination,
            @RequestParam(required = false) Prioriter priorite,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        // Appel du service pour filtrer les colis
        Page<ColisDTO> result = colisService.filterColis(statut, zoneId, villeDestination, priorite, pageable);

        // Retourner le résultat encapsulé dans ApiResponse
        return ResponseEntity.ok(new ApiResponse<>("Colis filtrés récupérés avec succès", result));
    }

    @GetMapping("/search")
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

}