package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ZoneDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/zones")
@Tag(name = "Zones", description = "API pour gérer les zones")

public class ZoneController {

    public final ZoneService service;

    public ZoneController(ZoneService service){
        this.service = service;
    }

    @PreAuthorize("hasRole('GESTIONNAIRE')")
    @PostMapping
    @Operation(summary = "Créer une zone", description = "Permet de créer une nouvelle zone")

    public ResponseEntity<ApiResponse<ZoneDTO>> createZone(@Valid @RequestBody ZoneDTO dto){
        ZoneDTO created = service.createZone(dto);
        return ResponseEntity.ok(new ApiResponse<>("Zone créée avec succès", created));
    }

    @PreAuthorize("hasAnyRole('GESTIONNAIRE','LIVREUR')")
    @GetMapping
    @Operation(summary = "Lister toutes les zones", description = "Récupère toutes les zones avec pagination")

    public ResponseEntity<ApiResponse<Page<ZoneDTO>>> getAllZones(
            @PageableDefault(size = 20, sort = "nom") Pageable pageable){
        Page<ZoneDTO> zones = service.getAllZones(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des zones récupérée avec succès", zones));
    }

    @PreAuthorize("hasAnyRole('GESTIONNAIRE','LIVREUR')")
    @GetMapping("{id}")
    @Operation(summary = "Récupérer une zone par ID", description = "Cherche une zone par son ID")

    public ResponseEntity<ApiResponse<ZoneDTO>> getZoneById(@PathVariable String id){
            ZoneDTO zone = service.getZoneById(id);
            if (zone == null){
                return ResponseEntity.status(404).body(new ApiResponse<>("Zone non trouvée", null));
            } else {
                return ResponseEntity.ok(new ApiResponse<>("Zone trouvée", zone));
            }
    }

    @PreAuthorize("hasRole('GESTIONNAIRE')")
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une zone", description = "Modifie les informations d'une zone existante")

    public ResponseEntity<ApiResponse<ZoneDTO>> updateZone(@PathVariable String id, @Valid @RequestBody ZoneDTO dto) {
        ZoneDTO updated = service.updateZone(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Zone mise à jour avec succès", updated));
    }

    @PreAuthorize("hasRole('GESTIONNAIRE')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une zone", description = "Supprime une zone par son ID")

    public ResponseEntity<ApiResponse<Void>> deleteZone(@PathVariable String id) {
        service.deleteZone(id);
        return ResponseEntity.ok(new ApiResponse<>("Zone supprimée avec succès", null));
    }
}
