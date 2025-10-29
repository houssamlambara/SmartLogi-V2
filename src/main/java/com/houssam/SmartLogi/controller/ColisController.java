package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.repository.ColisRepository;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ColisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/api/colis")
public class ColisController {

    private final ColisService service;

    public ColisController (ColisService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ColisDTO>> createColis(@Valid @RequestBody ColisDTO dto) {
        ColisDTO created = service.createColis(dto);
        return ResponseEntity.ok(new ApiResponse<>("Colis créé avec succès", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ColisDTO>>> getAllColis() {
        List<ColisDTO> liste = service.getAllColis();
        return ResponseEntity.ok(new ApiResponse<>("Liste des colis récupérée avec succès", liste));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ColisDTO>> getColisById(@PathVariable Long id) {
        ColisDTO colis = service.getColisById(id);
        if (colis != null) {
            return ResponseEntity.ok(new ApiResponse<>("Colis trouvé", colis));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Colis non trouvé", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteColis(@PathVariable Long id) {
        service.deleteColis(id);
        return ResponseEntity.ok(new ApiResponse<>("Colis supprimé avec succès", null));
    }
}
