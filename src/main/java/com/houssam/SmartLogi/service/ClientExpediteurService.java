package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.mapper.ClientExpediteurMapper;
import com.houssam.SmartLogi.repository.ClientExpediteurRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientExpediteurService {
    private final ClientExpediteurRepository repository;
    private final ClientExpediteurMapper mapper;

    public ClientExpediteurService(clientExpediteurRepository repository, ClientExpediteurMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ClientExpediteurMapper getMapper() {
        return mapper;
    }
}
