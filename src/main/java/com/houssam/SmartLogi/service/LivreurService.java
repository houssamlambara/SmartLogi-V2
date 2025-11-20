package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.security.config.SecurityConfig;
import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.enums.Role;
import com.houssam.SmartLogi.exception.ResourceNotFoundException;
import com.houssam.SmartLogi.mapper.LivreurMapper;
import com.houssam.SmartLogi.model.Livreur;
import com.houssam.SmartLogi.model.User;
import com.houssam.SmartLogi.model.Zone;
import com.houssam.SmartLogi.repository.LivreurRepository;
import com.houssam.SmartLogi.repository.ZoneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivreurService {

    private final LivreurRepository repository;
    private final LivreurMapper mapper;
    private final ZoneRepository zoneRepository;
    private final SecurityConfig securityConfig;

    public LivreurService(LivreurRepository repository, LivreurMapper mapper, ZoneRepository zoneRepository, SecurityConfig securityConfig) {
        this.repository = repository;
        this.mapper = mapper;
        this.zoneRepository = zoneRepository;
        this.securityConfig = securityConfig;
    }

    @Transactional
    public LivreurDTO createLivreur(LivreurDTO dto) {
        Livreur entity = mapper.toEntity(dto);

        Zone zone = zoneRepository.findById(dto.getZoneAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID " + dto.getZoneAssigneeId()));
        entity.setZoneAssignee(zone);

        entity.setEmail(dto.getEmail());
        User user = new User();
        user.setEmail(dto.getEmail());

        user.setPassword(securityConfig.passwordEncoder().encode(dto.getPassword()));
        user.setRole(Role.LIVREUR);
        entity.setUser(user);
        user.setLivreur(entity);

        Livreur saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public Page<LivreurDTO> getAllLivreurs(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    public LivreurDTO updateLivreur(String id, LivreurDTO dto) {
        Livreur livreur = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable avec l'ID " + id));

        livreur.setNom(dto.getNom());
        livreur.setPrenom(dto.getPrenom());
        livreur.setTelephone(dto.getTelephone());
        livreur.setVehicule(dto.getVehicule());

        if (dto.getZoneAssigneeId() != null) {
            Zone zone = zoneRepository.findById(dto.getZoneAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID " + dto.getZoneAssigneeId()));
            livreur.setZoneAssignee(zone);
        }
        Livreur updated = repository.save(livreur);
        return mapper.toDTO(updated);
    }

    public LivreurDTO getLivreurById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public void deleteLivreur(String id) {
        repository.deleteById(id);
    }

    public Page<LivreurDTO> searchLivreurs(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll(pageable)
                    .map(mapper::toDTO);
        }
        return repository.searchLivreurs(keyword.trim(), pageable)
                .map(mapper::toDTO);
    }
}
