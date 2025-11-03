package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.mapper.ClientExpediteurMapper;
import com.houssam.SmartLogi.model.ClientExpediteur;
import com.houssam.SmartLogi.repository.ClientExpediteurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<ClientExpediteurDTO> getAllClients(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    public ClientExpediteurDTO getClientById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public ClientExpediteurDTO updateClient(String id, ClientExpediteurDTO dto) {
        ClientExpediteur client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientExpediteur introuvable avec l'ID " + id));

        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setTelephone(dto.getTelephone());
        client.setEmail(dto.getEmail());
        client.setAdresse(dto.getAdresse());

        ClientExpediteur updated = repository.save(client);
        return mapper.toDTO(updated);
    }

    public void deleteClient(String id) {
        repository.deleteById(id);
    }
}
