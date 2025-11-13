package com.houssam.SmartLogi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.houssam.SmartLogi.dto.ZoneDTO;
import com.houssam.SmartLogi.service.ZoneService;
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

@WebMvcTest(ZoneController.class)
class ZoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ZoneService zoneService;

    @Autowired
    private ObjectMapper objectMapper;

    private ZoneDTO zoneDTO;

    @BeforeEach
    void setUp() {
        zoneDTO = new ZoneDTO();
        zoneDTO.setNom("Zone Nord");
        zoneDTO.setCodePostal("20000");
    }

    @Test
    void createZone_success() throws Exception {
        when(zoneService.createZone(any(ZoneDTO.class))).thenReturn(zoneDTO);

        mockMvc.perform(post("/api/zones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Zone créée avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Zone Nord"));

        verify(zoneService, times(1)).createZone(any(ZoneDTO.class));
    }

    @Test
    void getAllZones_success() throws Exception {
        Page<ZoneDTO> page = new PageImpl<>(List.of(zoneDTO));
        when(zoneService.getAllZones(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/zones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Liste des zones récupérée avec succès"))
                .andExpect(jsonPath("$.data.content[0].nom").value("Zone Nord"));

        verify(zoneService, times(1)).getAllZones(any(Pageable.class));
    }

    @Test
    void getZoneById_found() throws Exception {
        when(zoneService.getZoneById("zone1")).thenReturn(zoneDTO);

        mockMvc.perform(get("/api/zones/zone1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Zone trouvée"))
                .andExpect(jsonPath("$.data.nom").value("Zone Nord"));

        verify(zoneService, times(1)).getZoneById("zone1");
    }

    @Test
    void getZoneById_notFound() throws Exception {
        when(zoneService.getZoneById("zone999")).thenReturn(null);

        mockMvc.perform(get("/api/zones/zone999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Zone non trouvée"));

        verify(zoneService, times(1)).getZoneById("zone999");
    }

    @Test
    void updateZone_success() throws Exception {
        ZoneDTO updatedZone = new ZoneDTO();
        updatedZone.setNom("Zone Sud");
        updatedZone.setCodePostal("30000");

        when(zoneService.updateZone(eq("zone1"), any(ZoneDTO.class))).thenReturn(updatedZone);

        mockMvc.perform(put("/api/zones/zone1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedZone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Zone mise à jour avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Zone Sud"));

        verify(zoneService, times(1)).updateZone(eq("zone1"), any(ZoneDTO.class));
    }

    @Test
    void deleteZone_success() throws Exception {
        doNothing().when(zoneService).deleteZone("zone1");

        mockMvc.perform(delete("/api/zones/zone1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Zone supprimée avec succès"));

        verify(zoneService, times(1)).deleteZone("zone1");
    }
}
