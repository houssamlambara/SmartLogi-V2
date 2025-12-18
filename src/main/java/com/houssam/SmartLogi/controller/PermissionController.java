package com.houssam.SmartLogi.controller;

import com.houssam.SmartLogi.dto.PermissionDTO;
import com.houssam.SmartLogi.response.ApiResponse;
import com.houssam.SmartLogi.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('GESTIONNAIRE')")
@Tag(name = "Administration", description = "API pour la gestion des permissions et rôles (Gestionnaires uniquement)")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }


    @PostMapping("/permissions")
    @Operation(summary = "Créer une permission", description = "Permet au gestionnaire de créer une nouvelle permission")
    public ResponseEntity<ApiResponse<PermissionDTO>> createPermission(@Valid @RequestBody PermissionDTO dto) {
        PermissionDTO created = permissionService.createPermission(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Permission créée avec succès", created));
    }

    @GetMapping("/permissions")
    @Operation(summary = "Lister toutes les permissions", description = "Récupère la liste complète des permissions disponibles")
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> getAllPermissions() {
        List<PermissionDTO> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(new ApiResponse<>("Liste des permissions récupérée avec succès", permissions));
    }

    @GetMapping("/permissions/{id}")
    @Operation(summary = "Récupérer une permission", description = "Récupère les détails d'une permission par son ID")
    public ResponseEntity<ApiResponse<PermissionDTO>> getPermissionById(@PathVariable String id) {
        PermissionDTO permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(new ApiResponse<>("Permission récupérée avec succès", permission));
    }

    @PutMapping("/permissions/{id}")
    @Operation(summary = "Modifier une permission", description = "Permet de modifier une permission existante")
    public ResponseEntity<ApiResponse<PermissionDTO>> updatePermission(
            @PathVariable String id,
            @Valid @RequestBody PermissionDTO dto) {
        PermissionDTO updated = permissionService.updatePermission(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Permission mise à jour avec succès", updated));
    }

    @DeleteMapping("/permissions/{id}")
    @Operation(summary = "Supprimer une permission", description = "Supprime une permission et la retire de tous les rôles")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable String id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok(new ApiResponse<>("Permission supprimée avec succès", null));
    }

    @PostMapping("/roles/{roleId}/permissions/{permissionId}")
    @Operation(summary = "Assigner une permission à un rôle", description = "Ajoute une permission à un rôle spécifique")
    public ResponseEntity<ApiResponse<Void>> assignPermissionToRole(
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        permissionService.assignPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok(new ApiResponse<>("Permission assignée au rôle avec succès", null));
    }

    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    @Operation(summary = "Retirer une permission d'un rôle", description = "Retire une permission d'un rôle spécifique")
    public ResponseEntity<ApiResponse<Void>> removePermissionFromRole(
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        permissionService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok(new ApiResponse<>("Permission retirée du rôle avec succès", null));
    }

    @GetMapping("/roles/{roleId}/permissions")
    @Operation(summary = "Consulter les permissions d'un rôle", description = "Récupère toutes les permissions assignées à un rôle")
    public ResponseEntity<ApiResponse<Set<PermissionDTO>>> getPermissionsByRole(@PathVariable String roleId) {
        Set<PermissionDTO> permissions = permissionService.getPermissionsByRole(roleId);
        return ResponseEntity.ok(new ApiResponse<>("Permissions du rôle récupérées avec succès", permissions));
    }
}

