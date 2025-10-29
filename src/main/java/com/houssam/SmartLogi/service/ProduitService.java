package com.houssam.SmartLogi.service;


import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.mapper.ProduitMapper;
import com.houssam.SmartLogi.model.Produit;
import com.houssam.SmartLogi.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProduitService {

    private final ProduitRepository repository;
    private final ProduitMapper mapper;

    public ProduitService(ProduitRepository repository, ProduitMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProduitDTO createProduit(ProduitDTO dto){
        Produit entity = mapper.toEntity(dto);
        Produit saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public List<ProduitDTO> getAllProduits() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProduitDTO getProduitById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public void deleteProduit(Long id) {
        repository.deleteById(id);
    }
}
