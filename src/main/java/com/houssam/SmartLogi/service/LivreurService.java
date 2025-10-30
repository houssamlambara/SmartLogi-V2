package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.exception.ResourceNotFoundException;
import com.houssam.SmartLogi.mapper.LivreurMapper;
import com.houssam.SmartLogi.model.Livreur;
import com.houssam.SmartLogi.model.Zone;
import com.houssam.SmartLogi.repository.LivreurRepository;
import com.houssam.SmartLogi.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivreurService {

    private final LivreurRepository repository;
    private final LivreurMapper mapper;
    private final ZoneRepository zoneRepository;

    public LivreurService(LivreurRepository repository, LivreurMapper mapper, ZoneRepository zoneRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.zoneRepository = zoneRepository;
    }

    public LivreurDTO createLivreur(LivreurDTO dto) {
        Livreur entity = mapper.toEntity(dto);

        Zone zone = zoneRepository.findById(dto.getZoneAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID " + dto.getZoneAssigneeId()));
        entity.setZoneAssignee(zone);

        Livreur saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public List<LivreurDTO> getAllLivreurs() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public LivreurDTO getLivreurById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public void deleteLivreur(Long id) {
        repository.deleteById(id);
    }
}
