package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ZoneDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ZoneService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/zone")
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
    public ResponseEntity<ApiResponse<List<ZoneDTO>>> getAllZones(){
        List<ZoneDTO> zones = service.getAllZones();
        return ResponseEntity.ok(new ApiResponse<>("Liste des zones", zones));
    }

    @GetMapping("{id}")
        public ResponseEntity<ApiResponse<ZoneDTO>> getZoneById(@PathVariable Long id){
            ZoneDTO zone = service.getZoneById(id);
            if (zone == null){
                return ResponseEntity.status(404).body(new ApiResponse<>("Zone non trouvée", null));
            } else {
                return ResponseEntity.ok(new ApiResponse<>("Zone trouvée", zone));
            }
        }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteZone(@PathVariable Long id) {
        service.deleteZone(id);
        return ResponseEntity.ok(new ApiResponse<>("Zone supprimée avec succès", null));
    }
}
