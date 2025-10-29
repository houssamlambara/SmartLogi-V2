package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ClientExpediteurService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientExpediteurController {

    private final ClientExpediteurService service;

    public ClientExpediteurController(ClientExpediteurService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClientExpediteurDTO>> createClient(@Valid @RequestBody ClientExpediteurDTO dto) {
        ClientExpediteurDTO created = service.createClient(dto);
        return ResponseEntity.ok(new ApiResponse<>("Client créé avec succès",created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientExpediteurDTO>>> getAllClients() {
        List<ClientExpediteurDTO> clients = service.getAllClients();
        return ResponseEntity.ok(new ApiResponse<>("Liste des clients récupérée avec succès", clients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientExpediteurDTO>> getClientById(@PathVariable Long id) {
        ClientExpediteurDTO client = service.getClientById(id);
        if (client == null) {
            return ResponseEntity.ok(new ApiResponse<>("Client trouvé", client));
        }
        return ResponseEntity.status(404).body(new ApiResponse<>("Client non trouvé", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable Long id) {
        service.deleteClient(id);
        return ResponseEntity.ok(new ApiResponse<>("Clinet supprimé avec succès", null));
    }
}
