package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.service.ColisService;
import com.houssam.SmartLogi.service.LivreurService;
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
public class LivreurController {

    private final LivreurService livreurService;
    private final ColisService colisService;

    public LivreurController(LivreurService service, ColisService colisService) {
        this.livreurService = service;
        this.colisService = colisService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LivreurDTO>> createLivreur(@Valid @RequestBody LivreurDTO dto) {
        LivreurDTO created = livreurService.createLivreur(dto);
        return ResponseEntity.ok(new ApiResponse<>("Livreur créé avec succès", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LivreurDTO>>> getAllLivreurs(
            @PageableDefault(size = 20, sort = "nom") Pageable pageable) {
        Page<LivreurDTO> liste = livreurService.getAllLivreurs(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des livreurs récupérée avec succès", liste));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LivreurDTO>> getLivreurById(@PathVariable String id) {
        LivreurDTO livreur = livreurService.getLivreurById(id);
        if (livreur != null) {
            return ResponseEntity.ok(new ApiResponse<>("Livreur trouvé", livreur));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Livreur non trouvé", null));
        }
    }

    @GetMapping("/{id}/colis")
    public ResponseEntity<ApiResponse<Page<ColisDTO>>> getColisByLivreur(
            @PathVariable String id,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ColisDTO> colis = colisService.getColisByLivreurId(id, pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des colis assignés au livreur récupérée avec succès", colis));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LivreurDTO>> updateLivreur(@PathVariable String id, @Valid @RequestBody LivreurDTO dto) {
        LivreurDTO updated = livreurService.updateLivreur(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Livreur mis à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLivreur(@PathVariable String id) {
        livreurService.deleteLivreur(id);
        return ResponseEntity.ok(new ApiResponse<>("Livreur supprimé avec succès", null));
    }

    @GetMapping("/search")
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
