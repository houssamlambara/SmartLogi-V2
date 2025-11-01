package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.service.ColisService;
import com.houssam.SmartLogi.service.LivreurService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.houssam.SmartLogi.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/livreurs")
public class LivreurController {

    private final LivreurService service;
    private final ColisService colisService;

    public LivreurController(LivreurService service, ColisService colisService) {
        this.service = service;
        this.colisService = colisService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LivreurDTO>> createLivreur(@Valid @RequestBody LivreurDTO dto) {
        LivreurDTO created = service.createLivreur(dto);
        return ResponseEntity.ok(new ApiResponse<>("Livreur créé avec succès", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LivreurDTO>>> getAllLivreurs() {
        List<LivreurDTO> liste = service.getAllLivreurs();
        return ResponseEntity.ok(new ApiResponse<>("Liste des livreurs récupérée avec succès", liste));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LivreurDTO>> getLivreurById(@PathVariable String id) {
        LivreurDTO livreur = service.getLivreurById(id);
        if (livreur != null) {
            return ResponseEntity.ok(new ApiResponse<>("Livreur trouvé", livreur));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Livreur non trouvé", null));
        }
    }

    @GetMapping("/{id}/colis")
    public ResponseEntity<ApiResponse<List<ColisDTO>>> getColisByLivreur(@PathVariable String id) {
        List<ColisDTO> colis = colisService.getColisByLivreurId(id);
        return ResponseEntity.ok(new ApiResponse<>("Liste des colis assignés au livreur récupérée avec succès", colis));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLivreur(@PathVariable String id) {
        service.deleteLivreur(id);
        return ResponseEntity.ok(new ApiResponse<>("Livreur supprimé avec succès", null));
    }
}
