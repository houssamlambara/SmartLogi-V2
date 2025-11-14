package com.houssam.SmartLogi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProduitController.class)
class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProduitService produitService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProduitDTO produitDTO;

    @BeforeEach
    void setUp() {
        produitDTO = new ProduitDTO();
        produitDTO.setNom("Ordinateur Portable");
        produitDTO.setCategorie("Électronique");
        produitDTO.setPoids(2.5);
        produitDTO.setPrix(5000.0);
    }

    @Test
    void createProduit_success() throws Exception {
        when(produitService.createProduit(any(ProduitDTO.class))).thenReturn(produitDTO);

        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produitDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Produit créé avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Ordinateur Portable"))
                .andExpect(jsonPath("$.data.categorie").value("Électronique"))
                .andExpect(jsonPath("$.data.poids").value(2.5))
                .andExpect(jsonPath("$.data.prix").value(5000.0));

        verify(produitService, times(1)).createProduit(any(ProduitDTO.class));
    }

    @Test
    void getAllProduits_success() throws Exception {
        Page<ProduitDTO> page = new PageImpl<>(List.of(produitDTO));
        when(produitService.getAllProduits(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Liste des produits récupérée avec succès"))
                .andExpect(jsonPath("$.data.content[0].nom").value("Ordinateur Portable"));

        verify(produitService, times(1)).getAllProduits(any(Pageable.class));
    }

    @Test
    void getProduitById_found() throws Exception {
        when(produitService.getProduitById("produit1")).thenReturn(produitDTO);

        mockMvc.perform(get("/api/produits/produit1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Produit trouvé"))
                .andExpect(jsonPath("$.data.nom").value("Ordinateur Portable"))
                .andExpect(jsonPath("$.data.categorie").value("Électronique"));

        verify(produitService, times(1)).getProduitById("produit1");
    }

    @Test
    void getProduitById_notFound() throws Exception {
        when(produitService.getProduitById("produit999")).thenReturn(null);

        mockMvc.perform(get("/api/produits/produit999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produit non trouvé"));

        verify(produitService, times(1)).getProduitById("produit999");
    }

    @Test
    void updateProduit_success() throws Exception {
        ProduitDTO updatedProduit = new ProduitDTO();
        updatedProduit.setNom("Tablette");
        updatedProduit.setCategorie("Électronique");
        updatedProduit.setPoids(1.5);
        updatedProduit.setPrix(3000.0);

        when(produitService.updateProduit(eq("produit1"), any(ProduitDTO.class))).thenReturn(updatedProduit);

        mockMvc.perform(put("/api/produits/produit1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Produit mis à jour avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Tablette"))
                .andExpect(jsonPath("$.data.prix").value(3000.0));

        verify(produitService, times(1)).updateProduit(eq("produit1"), any(ProduitDTO.class));
    }

    @Test
    void deleteProduit_success() throws Exception {
        doNothing().when(produitService).deleteProduit("produit1");

        mockMvc.perform(delete("/api/produits/produit1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Produit supprimé avec succès"));

        verify(produitService, times(1)).deleteProduit("produit1");
    }
}

