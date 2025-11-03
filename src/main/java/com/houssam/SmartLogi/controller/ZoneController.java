package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ZoneDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ZoneService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/zones")
public class ZoneController {

    public final ZoneService service;

    public ZoneController(ZoneService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ZoneDTO>> createZone(@Valid @RequestBody ZoneDTO dto){
        ZoneDTO created = service.createZone(dto);
        return ResponseEntity.ok(new ApiResponse<>("Zone créée avec succès", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ZoneDTO>>> getAllZones(
            @PageableDefault(size = 20, sort = "nom") Pageable pageable){
        Page<ZoneDTO> zones = service.getAllZones(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des zones récupérée avec succès", zones));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ZoneDTO>> getZoneById(@PathVariable String id){
            ZoneDTO zone = service.getZoneById(id);
            if (zone == null){
                return ResponseEntity.status(404).body(new ApiResponse<>("Zone non trouvée", null));
            } else {
                return ResponseEntity.ok(new ApiResponse<>("Zone trouvée", zone));
            }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ZoneDTO>> updateZone(@PathVariable String id, @Valid @RequestBody ZoneDTO dto) {
        ZoneDTO updated = service.updateZone(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Zone mise à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteZone(@PathVariable String id) {
        service.deleteZone(id);
        return ResponseEntity.ok(new ApiResponse<>("Zone supprimée avec succès", null));
    }
}
