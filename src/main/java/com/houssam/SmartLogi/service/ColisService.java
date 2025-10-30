package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.exception.ResourceNotFoundException;
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

    private final ColisRepository colisRepository;
    private final ColisMapper colisMapper;
    private final LivreurRepository livreurRepository;
    private final ClientExpediteurRepository clientRepository;
    private final DestinataireRepository destinataireRepository;
    private final ZoneRepository zoneRepository;

    public ColisService(ColisRepository colisRepository, ColisMapper colisMapper,
                        LivreurRepository livreurRepository,
                        ClientExpediteurRepository clientRepository,
                        DestinataireRepository destinataireRepository,
                        ZoneRepository zoneRepository) {
        this.colisRepository = colisRepository;
        this.colisMapper = colisMapper;
        this.livreurRepository = livreurRepository;
        this.clientRepository = clientRepository;
        this.destinataireRepository = destinataireRepository;
        this.zoneRepository = zoneRepository;
    }

    public ColisDTO createColis(ColisDTO dto) {
        Colis colis = colisMapper.toEntity(dto);

        colis.setLivreur(livreurRepository.findById(dto.getLivreurId())
                .orElseThrow(() -> new ResourceNotFoundException("Livreur introuvable avec l'ID " + dto.getLivreurId())));

        colis.setClientExpediteur(clientRepository.findById(dto.getClientExpediteurId())
                .orElseThrow(() -> new ResourceNotFoundException("ClientExpediteur introuvable avec l'ID " + dto.getClientExpediteurId())));

        colis.setDestinataire(destinataireRepository.findById(dto.getDestinataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Destinataire introuvable avec l'ID " + dto.getDestinataireId())));

        colis.setZone(zoneRepository.findById(dto.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone introuvable avec l'ID " + dto.getZoneId())));

        Colis saved = colisRepository.save(colis);
        return colisMapper.toDTO(saved);
    }

    public List<ColisDTO> getAllColis() {
        return colisRepository.findAll()
                .stream()
                .map(colisMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ColisDTO getColisById(Long id) {
        return colisRepository.findById(id)
                .map(colisMapper::toDTO)
                .orElse(null);
    }

    public void deleteColis(Long id) {
        colisRepository.deleteById(id);
    }
}
