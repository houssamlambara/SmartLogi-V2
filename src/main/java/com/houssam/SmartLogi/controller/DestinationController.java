package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.DestinataireDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.DestinataireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinataireService service;

    public DestinationController(DestinataireService service){
        this.service=service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DestinataireDTO>> create(@RequestBody DestinataireDTO dto) {
        DestinataireDTO saved = service.createDestinataire(dto);
        return ResponseEntity.ok(new ApiResponse<>( "Client créé avec succès", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DestinataireDTO>>> getAll() {
        List<DestinataireDTO> liste = service.getAllDestinataires();
        return ResponseEntity.ok(new ApiResponse<>("Liste des Destinataire récupérée avec succès" ,liste));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinataireDTO>> getById(@PathVariable Long id){
        DestinataireDTO dto = service.getDestinataireById(id);
        if(dto!=null){
            return ResponseEntity.ok(new ApiResponse<>("Destinataire Trouvé", dto));
        }
        return ResponseEntity.status(404).body(new ApiResponse<>("Destinataire non trouvé", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinataireDTO>> deleteById(@PathVariable Long id){
        service.deleteDestinataire(id);
        return ResponseEntity.ok(new ApiResponse<>("Destinataire supprimé avec succès", null));
    }
}
