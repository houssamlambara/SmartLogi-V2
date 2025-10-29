package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.DestinataireDTO;
import com.houssam.SmartLogi.mapper.DestinataireMapper;
import com.houssam.SmartLogi.model.Destinataire;
import com.houssam.SmartLogi.repository.DestinataireRepository;
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

    public List<DestinataireDTO> getAllDestinataires() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public DestinataireDTO getDestinataireById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public void deleteDestinataire(Long id) {
        repository.deleteById(id);
    }

}
