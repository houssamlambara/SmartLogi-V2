package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.mapper.ClientExpediteurMapper;
import com.houssam.SmartLogi.model.ClientExpediteur;
import com.houssam.SmartLogi.repository.ClientExpediteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientExpediteurService {

    private final ClientExpediteurRepository repository;
    private final ClientExpediteurMapper mapper;

    public ClientExpediteurService(ClientExpediteurRepository repository, ClientExpediteurMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ClientExpediteurDTO createClient(ClientExpediteurDTO dto) {
        ClientExpediteur entity = mapper.toEntity(dto);
        ClientExpediteur saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public List<ClientExpediteurDTO> getAllClients() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClientExpediteurDTO getClientById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public void deleteClient(Long id) {
        repository.deleteById(id);
    }
}
