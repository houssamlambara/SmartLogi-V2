package com.houssam.SmartLogi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.DestinataireDTO;
import com.houssam.SmartLogi.service.ColisService;
import com.houssam.SmartLogi.service.DestinataireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
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

@WebMvcTest(controllers = DestinationController.class, excludeAutoConfiguration = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class DestinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DestinataireService destinataireService;

    @MockitoBean
    private ColisService colisService;

    @Autowired
    private ObjectMapper objectMapper;

    private DestinataireDTO destinataireDTO;

    @BeforeEach
    void setUp() {
        destinataireDTO = new DestinataireDTO();
        destinataireDTO.setNom("Idrissi");
        destinataireDTO.setPrenom("Karim");
        destinataireDTO.setEmail("karim.idrissi@email.com");
        destinataireDTO.setTelephone("0623456789");
        destinataireDTO.setAdresse("789 Boulevard Zerktouni, Marrakech");
    }

    @Test
    void createDestinataire_success() throws Exception {
        when(destinataireService.createDestinataire(any(DestinataireDTO.class))).thenReturn(destinataireDTO);

        mockMvc.perform(post("/api/destinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(destinataireDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Client créé avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Idrissi"))
                .andExpect(jsonPath("$.data.prenom").value("Karim"))
                .andExpect(jsonPath("$.data.email").value("karim.idrissi@email.com"))
                .andExpect(jsonPath("$.data.telephone").value("0623456789"));

        verify(destinataireService, times(1)).createDestinataire(any(DestinataireDTO.class));
    }

    @Test
    void getAllDestinataires_success() throws Exception {
        Page<DestinataireDTO> page = new PageImpl<>(List.of(destinataireDTO));
        when(destinataireService.getAllDestinataires(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/destinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Liste des Destinataire récupérée avec succès"))
                .andExpect(jsonPath("$.data.content[0].nom").value("Idrissi"))
                .andExpect(jsonPath("$.data.content[0].prenom").value("Karim"));

        verify(destinataireService, times(1)).getAllDestinataires(any(Pageable.class));
    }

    @Test
    void getDestinataireById_found() throws Exception {
        when(destinataireService.getDestinataireById("dest1")).thenReturn(destinataireDTO);

        mockMvc.perform(get("/api/destinations/dest1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Destinataire Trouvé"))
                .andExpect(jsonPath("$.data.nom").value("Idrissi"))
                .andExpect(jsonPath("$.data.email").value("karim.idrissi@email.com"));

        verify(destinataireService, times(1)).getDestinataireById("dest1");
    }

    @Test
    void getDestinataireById_notFound() throws Exception {
        when(destinataireService.getDestinataireById("dest999")).thenReturn(null);

        mockMvc.perform(get("/api/destinations/dest999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Destinataire non trouvé"));

        verify(destinataireService, times(1)).getDestinataireById("dest999");
    }

    @Test
    void getColisByDestinataire_success() throws Exception {
        ColisDTO colisDTO = new ColisDTO();
        colisDTO.setDescription("Colis pour destinataire");
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));

        when(colisService.getColisByDestinataireId(eq("dest1"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/destinations/dest1/colis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Colis du destinataire récupérés avec succès"))
                .andExpect(jsonPath("$.data.content[0].description").value("Colis pour destinataire"));

        verify(colisService, times(1)).getColisByDestinataireId(eq("dest1"), any(Pageable.class));
    }

    @Test
    void updateDestinataire_success() throws Exception {
        DestinataireDTO updatedDestinataire = new DestinataireDTO();
        updatedDestinataire.setNom("Tazi");
        updatedDestinataire.setPrenom("Samira");
        updatedDestinataire.setEmail("samira.tazi@email.com");
        updatedDestinataire.setTelephone("0634567890");
        updatedDestinataire.setAdresse("321 Avenue Hassan II, Fès");

        when(destinataireService.updateDestinataire(eq("dest1"), any(DestinataireDTO.class))).thenReturn(updatedDestinataire);

        mockMvc.perform(put("/api/destinations/dest1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDestinataire)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Destinataire mis à jour avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Tazi"))
                .andExpect(jsonPath("$.data.prenom").value("Samira"))
                .andExpect(jsonPath("$.data.email").value("samira.tazi@email.com"));

        verify(destinataireService, times(1)).updateDestinataire(eq("dest1"), any(DestinataireDTO.class));
    }

    @Test
    void deleteDestinataire_success() throws Exception {
        doNothing().when(destinataireService).deleteDestinataire("dest1");

        mockMvc.perform(delete("/api/destinations/dest1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Destinataire supprimé avec succès"));

        verify(destinataireService, times(1)).deleteDestinataire("dest1");
    }
}
