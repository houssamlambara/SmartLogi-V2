package com.houssam.SmartLogi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import com.houssam.SmartLogi.service.ColisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ColisController.class)
class ColisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ColisService colisService;

    @Autowired
    private ObjectMapper objectMapper;

    private ColisDTO colisDTO;

    @BeforeEach
    void setUp() {
        colisDTO = new ColisDTO();
        colisDTO.setDescription("Colis urgent contenant des documents");
        colisDTO.setPoids(2.5);
        colisDTO.setStatut(Statut.Creer);
        colisDTO.setPriorite(Prioriter.Haute);
        colisDTO.setVilleDestination("Casablanca");
        colisDTO.setClientExpediteurId("client1");
        colisDTO.setDestinataireId("dest1");
    }

    @Test
    void createColis_success() throws Exception {
        when(colisService.createColis(any(ColisDTO.class))).thenReturn(colisDTO);

        mockMvc.perform(post("/api/colis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(colisDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Colis créé avec succès"))
                .andExpect(jsonPath("$.data.description").value("Colis urgent contenant des documents"))
                .andExpect(jsonPath("$.data.poids").value(2.5))
                .andExpect(jsonPath("$.data.statut").value("Creer"))
                .andExpect(jsonPath("$.data.priorite").value("Haute"))
                .andExpect(jsonPath("$.data.villeDestination").value("Casablanca"));

        verify(colisService, times(1)).createColis(any(ColisDTO.class));
    }

    @Test
    void getAllColis_success() throws Exception {
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));
        when(colisService.getAllColis(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/colis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Liste des colis récupérée avec succès"))
                .andExpect(jsonPath("$.data.content[0].description").value("Colis urgent contenant des documents"));

        verify(colisService, times(1)).getAllColis(any(Pageable.class));
    }

    @Test
    void getColisById_found() throws Exception {
        when(colisService.getColisById("colis1")).thenReturn(colisDTO);

        mockMvc.perform(get("/api/colis/colis1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Colis trouvé"))
                .andExpect(jsonPath("$.data.description").value("Colis urgent contenant des documents"))
                .andExpect(jsonPath("$.data.villeDestination").value("Casablanca"));

        verify(colisService, times(1)).getColisById("colis1");
    }

    @Test
    void getColisById_notFound() throws Exception {
        when(colisService.getColisById("colis999")).thenReturn(null);

        mockMvc.perform(get("/api/colis/colis999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Colis non trouvé"));

        verify(colisService, times(1)).getColisById("colis999");
    }

    @Test
    void deleteColis_success() throws Exception {
        doNothing().when(colisService).deleteColis("colis1");

        mockMvc.perform(delete("/api/colis/colis1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Colis supprimé avec succès"));

        verify(colisService, times(1)).deleteColis("colis1");
    }

    @Test
    void updateStatut_success() throws Exception {
        ColisDTO updatedColis = new ColisDTO();
        updatedColis.setDescription("Colis urgent contenant des documents");
        updatedColis.setPoids(2.5);
        updatedColis.setStatut(Statut.En_Stock);
        updatedColis.setPriorite(Prioriter.Haute);
        updatedColis.setVilleDestination("Casablanca");
        updatedColis.setClientExpediteurId("client1");
        updatedColis.setDestinataireId("dest1");

        when(colisService.updateStatut(eq("colis1"), eq(Statut.En_Stock))).thenReturn(updatedColis);

        mockMvc.perform(patch("/api/colis/colis1/statut")
                        .param("nouveauStatut", "En_Stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Statut du colis mis à jour avec succès"))
                .andExpect(jsonPath("$.data.statut").value("En_Stock"));

        verify(colisService, times(1)).updateStatut(eq("colis1"), eq(Statut.En_Stock));
    }

    @Test
    void filterColis_withAllParams_success() throws Exception {
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));
        when(colisService.filterColis(
                eq(Statut.Creer),
                eq("zone1"),
                eq("Casablanca"),
                eq(Prioriter.Haute),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/api/colis/filter")
                        .param("statut", "Creer")
                        .param("zoneId", "zone1")
                        .param("villeDestination", "Casablanca")
                        .param("priorite", "Haute"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Colis filtrés récupérés avec succès"))
                .andExpect(jsonPath("$.data.content[0].villeDestination").value("Casablanca"));

        verify(colisService, times(1)).filterColis(
                eq(Statut.Creer),
                eq("zone1"),
                eq("Casablanca"),
                eq(Prioriter.Haute),
                any(Pageable.class)
        );
    }

    @Test
    void filterColis_withoutParams_success() throws Exception {
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));
        when(colisService.filterColis(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/api/colis/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Colis filtrés récupérés avec succès"));

        verify(colisService, times(1)).filterColis(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        );
    }

    @Test
    void searchColis_withKeyword_success() throws Exception {
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));
        when(colisService.searchColis(eq("urgent"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/colis/search")
                        .param("keyword", "urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Résultats de recherche pour : urgent"))
                .andExpect(jsonPath("$.data.content[0].description").value("Colis urgent contenant des documents"));

        verify(colisService, times(1)).searchColis(eq("urgent"), any(Pageable.class));
    }

    @Test
    void searchColis_withoutKeyword_success() throws Exception {
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));
        when(colisService.searchColis(isNull(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/colis/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tous les colis ont été récupérés"));

        verify(colisService, times(1)).searchColis(isNull(), any(Pageable.class));
    }

    @Test
    void getColisCountByZone_success() throws Exception {
        Map<String, Long> stats = new HashMap<>();
        stats.put("Casablanca", 10L);
        stats.put("Rabat", 5L);

        when(colisService.getColisCountByZone()).thenReturn(stats);

        mockMvc.perform(get("/api/colis/stats/by-zone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Statistiques par zone"))
                .andExpect(jsonPath("$.data.Casablanca").value(10))
                .andExpect(jsonPath("$.data.Rabat").value(5));

        verify(colisService, times(1)).getColisCountByZone();
    }

    @Test
    void getColisCountByStatut_success() throws Exception {
        Map<String, Long> stats = new HashMap<>();
        stats.put("Creer", 15L);
        stats.put("Collecter", 8L);
        stats.put("Livrer", 20L);

        when(colisService.getColisCountByStatut()).thenReturn(stats);

        mockMvc.perform(get("/api/colis/stats/by-statut"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Statistiques par statut"))
                .andExpect(jsonPath("$.data.Creer").value(15))
                .andExpect(jsonPath("$.data.Collecter").value(8))
                .andExpect(jsonPath("$.data.Livrer").value(20));

        verify(colisService, times(1)).getColisCountByStatut();
    }

    @Test
    void getColisCountByPriorite_success() throws Exception {
        Map<String, Long> stats = new HashMap<>();
        stats.put("Haute", 12L);
        stats.put("Normale", 25L);
        stats.put("Basse", 5L);

        when(colisService.getColisCountByPriorite()).thenReturn(stats);

        mockMvc.perform(get("/api/colis/stats/by-priorite"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Statistiques par priorité"))
                .andExpect(jsonPath("$.data.Haute").value(12))
                .andExpect(jsonPath("$.data.Normale").value(25))
                .andExpect(jsonPath("$.data.Basse").value(5));

        verify(colisService, times(1)).getColisCountByPriorite();
    }

    @Test
    void getStatsLivreur_success() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        stats.put("LivreurId", "livreur1");
        stats.put("NombreColis", 10);
        stats.put("PoidsTotal", 50.5);

        when(colisService.getStatistiqueLivreur("livreur1")).thenReturn(stats);

        mockMvc.perform(get("/api/colis/livreur/livreur1/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Statistiques du Livreur"))
                .andExpect(jsonPath("$.data.LivreurId").value("livreur1"))
                .andExpect(jsonPath("$.data.NombreColis").value(10))
                .andExpect(jsonPath("$.data.PoidsTotal").value(50.5));

        verify(colisService, times(1)).getStatistiqueLivreur("livreur1");
    }

    @Test
    void getStatsZone_success() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        stats.put("ZoneId", "zone1");
        stats.put("NombreColis", 20);
        stats.put("PoidsTotal", 100.0);

        when(colisService.getStatistiqueZone("zone1")).thenReturn(stats);

        mockMvc.perform(get("/api/colis/zone/zone1/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Statistiques de la Zone"))
                .andExpect(jsonPath("$.data.ZoneId").value("zone1"))
                .andExpect(jsonPath("$.data.NombreColis").value(20))
                .andExpect(jsonPath("$.data.PoidsTotal").value(100.0));

        verify(colisService, times(1)).getStatistiqueZone("zone1");
    }
}

