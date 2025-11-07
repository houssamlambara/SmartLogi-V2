package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.DestinataireDTO;
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

@RestController
@RequestMapping("/api/destinations")
@Tag(name = "Destinations", description = "API pour gérer les destinataires et leurs colis")

public class DestinationController {

    private final DestinataireService service;
    private final ColisService colisService;

    public DestinationController(DestinataireService service, ColisService colisService){
        this.service=service;
        this.colisService = colisService;
    }

    @PostMapping
    @Operation(summary = "Créer un destinataire", description = "Cette API permet de créer un nouveau destinataire")

    public ResponseEntity<ApiResponse<DestinataireDTO>> create(@RequestBody DestinataireDTO dto) {
        DestinataireDTO saved = service.createDestinataire(dto);
        return ResponseEntity.ok(new ApiResponse<>( "Client créé avec succès", saved));
    }

    @GetMapping
    @Operation(summary = "Lister tous les destinataires", description = "Récupère tous les destinataires avec pagination")

    public ResponseEntity<ApiResponse<Page<DestinataireDTO>>> getAll(
            @PageableDefault(size = 20, sort = "nom") Pageable pageable) {
        Page<DestinataireDTO> liste = service.getAllDestinataires(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des Destinataire récupérée avec succès" ,liste));
    }

    @GetMapping("/{id}/colis")
    @Operation(summary = "Récupérer les colis d'un destinataire", description = "Liste tous les colis assignés à un destinataire")

    public ResponseEntity<ApiResponse<Page<ColisDTO>>> getColisByDestinataire(
            @PathVariable String id,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ColisDTO> colis = colisService.getColisByDestinataireId(id, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>("Colis du destinataire récupérés avec succès", colis)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un destinataire par ID", description = "Cherche un destinataire par son ID")

    public ResponseEntity<ApiResponse<DestinataireDTO>> getById(@PathVariable String id){
        DestinataireDTO dto = service.getDestinataireById(id);
        if(dto!=null){
            return ResponseEntity.ok(new ApiResponse<>("Destinataire Trouvé", dto));
        }
        return ResponseEntity.status(404).body(new ApiResponse<>("Destinataire non trouvé", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un destinataire", description = "Modifie les informations d'un destinataire existant")

    public ResponseEntity<ApiResponse<DestinataireDTO>> updateDestinataire(@PathVariable String id, @Valid @RequestBody DestinataireDTO dto){
        DestinataireDTO updated = service.updateDestinataire(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Destinataire mis à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un destinataire", description = "Supprime un destinataire par son ID")

    public ResponseEntity<ApiResponse<DestinataireDTO>> deleteById(@PathVariable String id){
        service.deleteDestinataire(id);
        return ResponseEntity.ok(new ApiResponse<>("Destinataire supprimé avec succès", null));
    }
}
