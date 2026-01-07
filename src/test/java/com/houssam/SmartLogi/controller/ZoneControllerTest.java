package com.houssam.SmartLogi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.houssam.SmartLogi.dto.ZoneDTO;
import com.houssam.SmartLogi.model.Zone;
import com.houssam.SmartLogi.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ZoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ZoneDTO zoneDTO;

    @BeforeEach
    void setUp() {
        zoneRepository.deleteAll();

        zoneDTO = new ZoneDTO();
        zoneDTO.setNom("Zone Nord");
        zoneDTO.setCodePostal("20000");
    }

    @Test
    void createZone_success() throws Exception {
        mockMvc.perform(post("/api/zones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Zone créée avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Zone Nord"));

        List<Zone> zones = zoneRepository.findAll();
        assertThat(zones).hasSize(1);
        assertThat(zones.get(0).getNom()).isEqualTo("Zone Nord");
        assertThat(zones.get(0).getCodePostal()).isEqualTo("20000");
    }

    @Test
    void getAllZones_success() throws Exception {
        Zone zone1 = new Zone();
        zone1.setNom("Zone Nord");
        zone1.setCodePostal("20000");
        zoneRepository.save(zone1);

        Zone zone2 = new Zone();
        zone2.setNom("Zone Sud");
        zone2.setCodePostal("30000");
        zoneRepository.save(zone2);

        mockMvc.perform(get("/api/zones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Liste des zones récupérée avec succès"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(2));
    }

    @Test
    void getZoneById_found() throws Exception {
        Zone zone = new Zone();
        zone.setNom("Zone Nord");
        zone.setCodePostal("20000");
        Zone savedZone = zoneRepository.save(zone);

        mockMvc.perform(get("/api/zones/" + savedZone.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Zone trouvée"))
                .andExpect(jsonPath("$.data.nom").value("Zone Nord"));
    }

    @Test
    void getZoneById_notFound() throws Exception {
        mockMvc.perform(get("/api/zones/id-inexistant"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Zone non trouvée"));
    }

    @Test
    void updateZone_success() throws Exception {
        Zone zone = new Zone();
        zone.setNom("Zone Nord");
        zone.setCodePostal("20000");
        Zone savedZone = zoneRepository.save(zone);

        ZoneDTO updatedDTO = new ZoneDTO();
        updatedDTO.setNom("Zone Sud");
        updatedDTO.setCodePostal("30000");

        mockMvc.perform(put("/api/zones/" + savedZone.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Zone mise à jour avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Zone Sud"));

        Zone updatedZone = zoneRepository.findById(savedZone.getId()).orElseThrow();
        assertThat(updatedZone.getNom()).isEqualTo("Zone Sud");
        assertThat(updatedZone.getCodePostal()).isEqualTo("30000");
    }

    @Test
    void deleteZone_success() throws Exception {
        Zone zone = new Zone();
        zone.setNom("Zone à supprimer");
        zone.setCodePostal("40000");
        Zone savedZone = zoneRepository.save(zone);

        assertThat(zoneRepository.findById(savedZone.getId())).isPresent();

        mockMvc.perform(delete("/api/zones/" + savedZone.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Zone supprimée avec succès"));

        assertThat(zoneRepository.findById(savedZone.getId())).isEmpty();
        assertThat(zoneRepository.count()).isEqualTo(0);
    }
}
