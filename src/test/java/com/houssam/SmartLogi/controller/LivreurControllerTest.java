package com.houssam.SmartLogi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.service.ColisService;
import com.houssam.SmartLogi.service.LivreurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivreurController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@WithMockUser
class LivreurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LivreurService livreurService;

    @MockitoBean
    private ColisService colisService;

    @Autowired
    private ObjectMapper objectMapper;

    private LivreurDTO livreurDTO;

    @BeforeEach
    void setUp() {
        livreurDTO = new LivreurDTO();
        livreurDTO.setNom("Alami");
        livreurDTO.setPrenom("Ahmed");
        livreurDTO.setTelephone("0612345678");
        livreurDTO.setVehicule("Moto");
        livreurDTO.setZoneAssigneeId("zone1");
    }

    @Test
    void createLivreur_success() throws Exception {
        when(livreurService.createLivreur(any(LivreurDTO.class))).thenReturn(livreurDTO);

        mockMvc.perform(post("/api/livreurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livreurDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Livreur créé avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Alami"))
                .andExpect(jsonPath("$.data.prenom").value("Ahmed"))
                .andExpect(jsonPath("$.data.telephone").value("0612345678"))
                .andExpect(jsonPath("$.data.vehicule").value("Moto"));

        verify(livreurService, times(1)).createLivreur(any(LivreurDTO.class));
    }

    @Test
    void getAllLivreurs_success() throws Exception {
        Page<LivreurDTO> page = new PageImpl<>(List.of(livreurDTO));
        when(livreurService.getAllLivreurs(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/livreurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Liste des livreurs récupérée avec succès"))
                .andExpect(jsonPath("$.data.content[0].nom").value("Alami"))
                .andExpect(jsonPath("$.data.content[0].prenom").value("Ahmed"));

        verify(livreurService, times(1)).getAllLivreurs(any(Pageable.class));
    }

    @Test
    void getLivreurById_found() throws Exception {
        when(livreurService.getLivreurById("livreur1")).thenReturn(livreurDTO);

        mockMvc.perform(get("/api/livreurs/livreur1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Livreur trouvé"))
                .andExpect(jsonPath("$.data.nom").value("Alami"))
                .andExpect(jsonPath("$.data.prenom").value("Ahmed"));

        verify(livreurService, times(1)).getLivreurById("livreur1");
    }

    @Test
    void getLivreurById_notFound() throws Exception {
        when(livreurService.getLivreurById("livreur999")).thenReturn(null);

        mockMvc.perform(get("/api/livreurs/livreur999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Livreur non trouvé"));

        verify(livreurService, times(1)).getLivreurById("livreur999");
    }

    @Test
    void getColisByLivreur_success() throws Exception {
        ColisDTO colisDTO = new ColisDTO();
        colisDTO.setDescription("Colis test");
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));

        when(colisService.getColisByLivreurId(eq("livreur1"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/livreurs/livreur1/colis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Liste des colis assignés au livreur récupérée avec succès"))
                .andExpect(jsonPath("$.data.content[0].description").value("Colis test"));

        verify(colisService, times(1)).getColisByLivreurId(eq("livreur1"), any(Pageable.class));
    }

    @Test
    void updateLivreur_success() throws Exception {
        LivreurDTO updatedLivreur = new LivreurDTO();
        updatedLivreur.setNom("Bennani");
        updatedLivreur.setPrenom("Fatima");
        updatedLivreur.setTelephone("0687654321");
        updatedLivreur.setVehicule("Voiture");
        updatedLivreur.setZoneAssigneeId("zone2");

        when(livreurService.updateLivreur(eq("livreur1"), any(LivreurDTO.class))).thenReturn(updatedLivreur);

        mockMvc.perform(put("/api/livreurs/livreur1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLivreur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Livreur mis à jour avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Bennani"))
                .andExpect(jsonPath("$.data.prenom").value("Fatima"))
                .andExpect(jsonPath("$.data.vehicule").value("Voiture"));

        verify(livreurService, times(1)).updateLivreur(eq("livreur1"), any(LivreurDTO.class));
    }

    @Test
    void deleteLivreur_success() throws Exception {
        doNothing().when(livreurService).deleteLivreur("livreur1");

        mockMvc.perform(delete("/api/livreurs/livreur1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Livreur supprimé avec succès"));

        verify(livreurService, times(1)).deleteLivreur("livreur1");
    }

    @Test
    void searchLivreurs_withKeyword_success() throws Exception {
        Page<LivreurDTO> page = new PageImpl<>(List.of(livreurDTO));
        when(livreurService.searchLivreurs(eq("Ahmed"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/livreurs/search")
                        .param("keyword", "Ahmed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Résultats de recherche pour : Ahmed"))
                .andExpect(jsonPath("$.data.content[0].prenom").value("Ahmed"));

        verify(livreurService, times(1)).searchLivreurs(eq("Ahmed"), any(Pageable.class));
    }

    @Test
    void searchLivreurs_withoutKeyword_success() throws Exception {
        Page<LivreurDTO> page = new PageImpl<>(List.of(livreurDTO));
        when(livreurService.searchLivreurs(isNull(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/livreurs/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tous les livreurs ont été récupérés"));

        verify(livreurService, times(1)).searchLivreurs(isNull(), any(Pageable.class));
    }
}

