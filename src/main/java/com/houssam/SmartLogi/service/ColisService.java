package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.mapper.ColisMapper;
import com.houssam.SmartLogi.model.ClientExpediteur;
import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.model.Livreur;
import com.houssam.SmartLogi.model.Zone;
import com.houssam.SmartLogi.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColisService {

    private final ColisRepository repository;
    private final ColisMapper mapper;
    private final LivreurRepository livreurRepository;
    private final ClientExpediteurRepository clientRepository;
    private final DestinataireRepository destinataireRepository;
    private final ZoneRepository zoneRepository;

    public ColisService(ColisRepository repository, ColisMapper mapper,
                        LivreurRepository livreurRepository,
                        ClientExpediteurRepository clientRepository,
                        DestinataireRepository destinataireRepository,
                        ZoneRepository zoneRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.livreurRepository = livreurRepository;
        this.clientRepository = clientRepository;
        this.destinataireRepository = destinataireRepository;
        this.zoneRepository = zoneRepository;
    }

    public ColisDTO createColis(ColisDTO dto) {
        Colis entity = mapper.toEntity(dto);

        // Affectation des relations
        entity.setLivreur(livreurRepository.findById(dto.getLivreurId()).orElse(null));
        entity.setClientExpediteur(clientRepository.findById(dto.getClientExpediteurId()).orElse(null));
        entity.setDestinataire(destinataireRepository.findById(dto.getDestinataireId()).orElse(null));
        entity.setZone(zoneRepository.findById(dto.getZoneId()).orElse(null));

        Colis saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public List<ColisDTO> getAllColis() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ColisDTO getColisById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElse(null);
    }

    public void deleteColis(Long id) {
        repository.deleteById(id);
    }
}
