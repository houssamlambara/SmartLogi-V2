package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.ClientExpediteurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "API pour gérer les clients expéditeurs")
public class ClientExpediteurController {

    private final ClientExpediteurService clientService;

    public ClientExpediteurController(ClientExpediteurService service) {
        this.clientService = service;
    }

    @PostMapping
    @Operation(summary = "Créer un client expéditeur", description = "Cette API permet de créer un nouveau client expéditeur")
    public ResponseEntity<ApiResponse<ClientExpediteurDTO>> createClient(@Valid @RequestBody ClientExpediteurDTO dto) {
        ClientExpediteurDTO created = clientService.createClient(dto);
        return ResponseEntity.ok(new ApiResponse<>("Client créé avec succès",created));
    }

    @GetMapping
    @Operation(summary = "Lister tous les clients", description = "Récupère la liste paginée des clients expéditeurs")
    public ResponseEntity<ApiResponse<Page<ClientExpediteurDTO>>> getAllClients(
            @PageableDefault(size = 20, sort = "nom") Pageable pageable) {
        Page<ClientExpediteurDTO> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(new ApiResponse<>("Liste des clients récupérée avec succès", clients));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un client par ID", description = "Récupère les informations d'un client expéditeur selon son ID")
    public ResponseEntity<ApiResponse<ClientExpediteurDTO>> getClientById(@PathVariable String id) {
        ClientExpediteurDTO client = clientService.getClientById(id);
        if (client != null) {
            return ResponseEntity.ok(new ApiResponse<>("Client trouvé", client));
        }
        return ResponseEntity.status(404).body(new ApiResponse<>("Client non trouvé", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un client", description = "Met à jour les informations d'un client existant")
    public ResponseEntity<ApiResponse<ClientExpediteurDTO>> updateClient(@PathVariable String id, @Valid @RequestBody ClientExpediteurDTO dto) {
        ClientExpediteurDTO updated = clientService.updateClient(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Client mis à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client", description = "Supprime un client expéditeur selon son ID")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok(new ApiResponse<>("Clinet supprimé avec succès", null));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des clients", description = "Recherche des clients expéditeurs par mot-clé (nom, prénom, téléphone, email, adresse)")

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
