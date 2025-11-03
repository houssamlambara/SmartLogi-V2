package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.DestinataireDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ColisService;
import com.houssam.SmartLogi.service.DestinataireService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinataireService service;
    private final ColisService colisService;

    public DestinationController(DestinataireService service, ColisService colisService){
        this.service=service;
        this.colisService = colisService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DestinataireDTO>> create(@RequestBody DestinataireDTO dto) {
        DestinataireDTO saved = service.createDestinataire(dto);
        return ResponseEntity.ok(new ApiResponse<>( "Client créé avec succès", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DestinataireDTO>>> getAll(
            @PageableDefault(size = 20, sort = "nom") Pageable pageable) {
        Page<DestinataireDTO> liste = service.getAllDestinataires(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des Destinataire récupérée avec succès" ,liste));
    }

    @GetMapping("/{id}/colis")
    public ResponseEntity<ApiResponse<Page<ColisDTO>>> getColisByDestinataire(
            @PathVariable String id,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ColisDTO> colis = colisService.getColisByDestinataireId(id, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>("Colis du destinataire récupérés avec succès", colis)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinataireDTO>> getById(@PathVariable String id){
        DestinataireDTO dto = service.getDestinataireById(id);
        if(dto!=null){
            return ResponseEntity.ok(new ApiResponse<>("Destinataire Trouvé", dto));
        }
        return ResponseEntity.status(404).body(new ApiResponse<>("Destinataire non trouvé", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinataireDTO>> updateDestinataire(@PathVariable String id, @Valid @RequestBody DestinataireDTO dto){
        DestinataireDTO updated = service.updateDestinataire(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Destinataire mis à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinataireDTO>> deleteById(@PathVariable String id){
        service.deleteDestinataire(id);
        return ResponseEntity.ok(new ApiResponse<>("Destinataire supprimé avec succès", null));
    }
}
