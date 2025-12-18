package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.PermissionDTO;
import com.houssam.SmartLogi.exception.ResourceNotFoundException;
import com.houssam.SmartLogi.model.Permission;
import com.houssam.SmartLogi.model.Role;
import com.houssam.SmartLogi.security.repository.PermissionRepository;
import com.houssam.SmartLogi.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public PermissionService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public PermissionDTO createPermission(PermissionDTO dto) {
        // Vérifier si la permission existe déjà
        if (permissionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Une permission avec ce nom existe déjà : " + dto.getName());
        }

        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());

        Permission saved = permissionRepository.save(permission);
        return toDTO(saved);
    }

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PermissionDTO getPermissionById(String id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission introuvable avec l'ID : " + id));
        return toDTO(permission);
    }

    @Transactional
    public PermissionDTO updatePermission(String id, PermissionDTO dto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission introuvable avec l'ID : " + id));

        // Vérifier si le nouveau nom n'existe pas déjà (sauf si c'est le même)
        if (!permission.getName().equals(dto.getName()) && permissionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Une permission avec ce nom existe déjà : " + dto.getName());
        }

        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());

        Permission updated = permissionRepository.save(permission);
        return toDTO(updated);
    }

    @Transactional
    public void deletePermission(String id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission introuvable avec l'ID : " + id));

        // Retirer la permission de tous les rôles qui la possèdent
        for (Role role : permission.getRoles()) {
            role.getPermissions().remove(permission);
        }

        permissionRepository.delete(permission);
    }


    @Transactional
    public void assignPermissionToRole(String roleId, String permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable avec l'ID : " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission introuvable avec l'ID : " + permissionId));

        // Vérifier si la permission n'est pas déjà assignée
        if (role.getPermissions().contains(permission)) {
            throw new IllegalArgumentException("Cette permission est déjà assignée à ce rôle");
        }

        role.getPermissions().add(permission);
        roleRepository.save(role);
    }


    @Transactional
    public void removePermissionFromRole(String roleId, String permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable avec l'ID : " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission introuvable avec l'ID : " + permissionId));

        // Vérifier si la permission est bien assignée
        if (!role.getPermissions().contains(permission)) {
            throw new IllegalArgumentException("Cette permission n'est pas assignée à ce rôle");
        }

        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }

    public Set<PermissionDTO> getPermissionsByRole(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Rôle introuvable avec l'ID : " + roleId));

        return role.getPermissions().stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    private PermissionDTO toDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setName(permission.getName());
        dto.setDescription(permission.getDescription());
        return dto;
    }
}
