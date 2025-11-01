package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ColisService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<List<ColisDTO>>> getAllColis() {
        List<ColisDTO> liste = colisService.getAllColis();
        return ResponseEntity.ok(new ApiResponse<>("Liste des colis récupérée avec succès", liste));
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
}
