package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ClientExpediteurService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/clients")
public class ClientExpediteurController {

    private final ClientExpediteurService clientService;

    public ClientExpediteurController(ClientExpediteurService service) {
        this.clientService = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClientExpediteurDTO>> createClient(@Valid @RequestBody ClientExpediteurDTO dto) {
        ClientExpediteurDTO created = clientService.createClient(dto);
        return ResponseEntity.ok(new ApiResponse<>("Client créé avec succès",created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ClientExpediteurDTO>>> getAllClients(
            @PageableDefault(size = 20, sort = "nom") Pageable pageable) {
        Page<ClientExpediteurDTO> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des clients récupérée avec succès", clients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientExpediteurDTO>> getClientById(@PathVariable String id) {
        ClientExpediteurDTO client = clientService.getClientById(id);
        if (client != null) {
            return ResponseEntity.ok(new ApiResponse<>("Client trouvé", client));
        }
        return ResponseEntity.status(404).body(new ApiResponse<>("Client non trouvé", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientExpediteurDTO>> updateClient(@PathVariable String id, @Valid @RequestBody ClientExpediteurDTO dto) {
        ClientExpediteurDTO updated = clientService.updateClient(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Client mis à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok(new ApiResponse<>("Clinet supprimé avec succès", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ClientExpediteurDTO>>> searchClients(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "nom", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ClientExpediteurDTO> result = clientService.searchClients(keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(
                (keyword == null || keyword.isBlank())
                        ? "Tous les clients ont été récupérés"
                        : "Résultats de recherche pour : " + keyword,
                result
        ));
    }
}
