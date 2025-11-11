package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.mapper.ProduitMapper;
import com.houssam.SmartLogi.model.Produit;
import com.houssam.SmartLogi.repository.ProduitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {

    @InjectMocks
    private ProduitService produitService;

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private ProduitMapper mapper;

    @Test
    void creatProduit_success(){
        ProduitDTO produitDTO = new ProduitDTO();
        produitDTO.setNom("Produit1");

        Produit entity = new Produit();
        Produit savedEntity = new Produit();
        ProduitDTO resultDTO = new ProduitDTO();

        when(mapper.toEntity(produitDTO)).thenReturn(entity);
        when(produitRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(resultDTO);

        ProduitDTO result = produitService.createProduit(produitDTO);

        assertNotNull(result);
        verify(produitRepository, times(1)).save(entity);
        verify(mapper, times(1)).toDTO(savedEntity);
    }

    @Test
    void getAllProduit_success(){
        Produit produit = new Produit();
        Page<Produit> page = new PageImpl<>(List.of(produit));

        when(produitRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(mapper.toDTO(produit)).thenReturn(new ProduitDTO());

        Page<ProduitDTO> result = produitService.getAllProduits(PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        verify(mapper, times(1)).toDTO(produit);
    }

    @Test
    void getProduitById_found(){
        Produit produit = new Produit();
        ProduitDTO produitDTO = new ProduitDTO();

        when(produitRepository.findById("id1")).thenReturn(Optional.of(produit));
        when(mapper.toDTO(produit)).thenReturn(produitDTO);

        ProduitDTO result = produitService.getProduitById("id1");

        assertNotNull(result);
        verify(produitRepository, times(1)).findById("id1");
        verify(mapper, times(1)).toDTO(produit);
    }

    @Test
    void getProduitById_notFound(){
        when(produitRepository.findById("id1")).thenReturn(Optional.empty());

        ProduitDTO result = produitService.getProduitById("id1");

        assertNull(result);
        verify(produitRepository, times(1)).findById("id1");
    }

    @Test
    void updateProduit_success(){
        Produit produit = new Produit();
        ProduitDTO produitDTO = new ProduitDTO();
        Produit updated = new Produit();
        ProduitDTO resultDTO = new ProduitDTO();

        when(produitRepository.findById("id1")).thenReturn(Optional.of(produit));
        when(produitRepository.save(produit)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(resultDTO);

        ProduitDTO result = produitService.updateProduit("id1", produitDTO);

        assertNotNull(result);
        verify(produitRepository, times(1)).save(produit);
        verify(mapper, times(1)).toDTO(updated);
    }

    @Test
    void deteleProduit_success(){
        doNothing().when(produitRepository).deleteById("id1");
        produitService.deleteProduit("id1");
        verify(produitRepository, times(1)).deleteById("id1");
    }


}
