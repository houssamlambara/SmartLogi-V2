package com.houssam.SmartLogi.service;


import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.mapper.ProduitMapper;
import com.houssam.SmartLogi.model.Produit;
import com.houssam.SmartLogi.repository.ProduitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<ProduitDTO> getAllProduits(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }
    
    public List<ProduitDTO> getAllProduits() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProduitDTO getProduitById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public ProduitDTO updateProduit(String id, ProduitDTO dto) {
        Produit produit = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID " + id));

        produit.setNom(dto.getNom());
        produit.setCategorie(dto.getCategorie());
        produit.setPoids(dto.getPoids());
        produit.setPrix(dto.getPrix());

        Produit updated = repository.save(produit);
        return mapper.toDTO(updated);
    }

    public void deleteProduit(String id) {
        repository.deleteById(id);
    }
}
