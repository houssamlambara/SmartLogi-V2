package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.security.config.SecurityConfig;
import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.mapper.ClientExpediteurMapper;
import com.houssam.SmartLogi.model.ClientExpediteur;
import com.houssam.SmartLogi.model.Role;
import com.houssam.SmartLogi.model.User;
import com.houssam.SmartLogi.repository.ClientExpediteurRepository;
import com.houssam.SmartLogi.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.houssam.SmartLogi.service.SecurityContextService;

@Service
public class ClientExpediteurService {

    private final ClientExpediteurRepository repository;
    private final ClientExpediteurMapper mapper;
    private final SecurityConfig securityConfig;
    private final SecurityContextService securityContextService;
    private final RoleRepository roleRepository;

    public ClientExpediteurService(ClientExpediteurRepository repository, ClientExpediteurMapper mapper, SecurityConfig securityConfig, SecurityContextService securityContextService, RoleRepository roleRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.securityConfig = securityConfig;
        this.securityContextService = securityContextService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public ClientExpediteurDTO createClient(ClientExpediteurDTO dto) {
        ClientExpediteur entity = mapper.toEntity(dto);

        entity.setEmail(dto.getEmail());
        User user = new User();
        user.setEmail(dto.getEmail());

        user.setPassword(securityConfig.passwordEncoder().encode(dto.getPassword()));

        // Récupérer le rôle CLIENT depuis la base de données
        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new RuntimeException("Rôle CLIENT non trouvé dans la base"));
        user.setRole(clientRole);

        entity.setUser(user);
        user.setClientExpediteur(entity);

        ClientExpediteur saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public Page<ClientExpediteurDTO> getAllClients(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    public ClientExpediteurDTO getClientById(String id) {
        ClientExpediteur client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'ID " + id));

        // Si l'utilisateur est un CLIENT, il ne peut voir que son propre profil
        if (securityContextService.isClient()) {
            String currentUserId = securityContextService.getCurrentUserId();
            if (!client.getUser().getId().equals(currentUserId)) {
                throw new RuntimeException("Accès refusé : vous ne pouvez consulter que votre propre profil");
            }
        }

        return mapper.toDTO(client);
    }

    public ClientExpediteurDTO updateClient(String id, ClientExpediteurDTO dto) {
        ClientExpediteur client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientExpediteur introuvable avec l'ID " + id));

        if (securityContextService.isClient()) {
            String currentUserId = securityContextService.getCurrentUserId();
            if (!client.getUser().getId().equals(currentUserId)) {
                throw new RuntimeException("Accès refusé : vous ne pouvez modifier que votre propre profil");
            }
        }

        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setTelephone(dto.getTelephone());
        if (client.getUser() != null && dto.getEmail() != null) {
            client.getUser().setEmail(dto.getEmail());
        }
        client.setAdresse(dto.getAdresse());

        ClientExpediteur updated = repository.save(client);
        return mapper.toDTO(updated);
    }

    public void deleteClient(String id) {
        repository.deleteById(id);
    }

    public Page<ClientExpediteurDTO> searchClients(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll(pageable).map(mapper::toDTO);
        }
        return repository.searchClients(keyword.trim(), pageable)
                .map(mapper::toDTO);
    }
}
