package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.DestinataireDTO;
import com.houssam.SmartLogi.mapper.DestinataireMapper;
import com.houssam.SmartLogi.model.Destinataire;
import com.houssam.SmartLogi.repository.DestinataireRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DestinataireService {

    private final DestinataireRepository repository;
    private final DestinataireMapper mapper;

    public DestinataireService(DestinataireRepository repository, DestinataireMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public DestinataireDTO createDestinataire(DestinataireDTO dto) {
        Destinataire entity = mapper.toEntity(dto);
        Destinataire saved = repository.save(entity);
        return mapper.toDTO(saved);

    }

    public Page<DestinataireDTO> getAllDestinataires(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    public List<DestinataireDTO> getAllDestinataires() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public DestinataireDTO getDestinataireById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public DestinataireDTO updateDestinataire(String id, DestinataireDTO dto){
        Destinataire destinataire = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destinataire introuvable avec l'ID " + id));
        destinataire.setNom(dto.getNom());
        destinataire.setPrenom(dto.getPrenom());
        destinataire.setTelephone(dto.getTelephone());
        destinataire.setEmail(dto.getEmail());
        destinataire.setAdresse(dto.getAdresse());
        Destinataire updated = repository.save(destinataire);
        return mapper.toDTO(updated);
    }

    public void deleteDestinataire(String id) {
        repository.deleteById(id);
    }

}
