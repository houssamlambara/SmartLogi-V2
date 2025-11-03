package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import com.houssam.SmartLogi.exception.ResourceNotFoundException;
import com.houssam.SmartLogi.mapper.ColisMapper;
import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Page<ColisDTO> getAllColis(Pageable pageable) {
        return colisRepository.findAll(pageable)
                .map(colisMapper::toDTO);
    }

    public Page<ColisDTO> getColisByDestinataireId(String destinataireId, Pageable pageable) {
        // 1. Vérifier que le destinataire existe
        destinataireRepository.findById(destinataireId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destinataire introuvable avec l'ID " + destinataireId));

        // 2. Récupérer et mapper les colis avec pagination
        return colisRepository.findByDestinataireId(destinataireId, pageable)
                .map(colisMapper::toDTO);
    }

    public Page<ColisDTO> getColisByLivreurId(String livreurId, Pageable pageable) {
        // 1. Vérifier que le livreur existe
        livreurRepository.findById(livreurId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livreur introuvable avec l'ID " + livreurId));

        // 2. Récupérer et mapper les colis assignés à ce livreur avec pagination
        return colisRepository.findByLivreurId(livreurId, pageable)
                .map(colisMapper::toDTO);
    }

    public ColisDTO getColisById(String id) {
        return colisRepository.findById(id)
                .map(colisMapper::toDTO)
                .orElse(null);
    }

    public ColisDTO updateStatut(String colisId, Statut nouveauStatut) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new ResourceNotFoundException("Colis introuvable avec l'ID " + colisId));
        colis.setStatut(nouveauStatut);
        Colis updated = colisRepository.save(colis);
        return colisMapper.toDTO(updated);
    }

    public void deleteColis(String id) {
        colisRepository.deleteById(id);
    }

    public Page<ColisDTO> filterColis(
            Statut statut,
            String zoneId,
            String villeDestination,
            Prioriter priorite,
            Pageable pageable
    ) {
        return colisRepository.filterColis(statut, zoneId, villeDestination, priorite, pageable)
                .map(colisMapper::toDTO);
    }

    public Page<ColisDTO> searchColis(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return colisRepository.findAll(pageable).map(colisMapper::toDTO);
        }
        return colisRepository.searchColis(keyword.trim(), pageable)
                .map(colisMapper::toDTO);
    }
}
