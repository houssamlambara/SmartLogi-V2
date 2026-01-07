package com.houssam.SmartLogi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.service.ClientExpediteurService;
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

@WebMvcTest(controllers = ClientExpediteurController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
class ClientExpediteurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientExpediteurService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientExpediteurDTO clientDTO;

    @BeforeEach
    void setUp() {
        clientDTO = new ClientExpediteurDTO();
        clientDTO.setNom("Alaoui");
        clientDTO.setPrenom("Mohammed");
        clientDTO.setEmail("mohammed.alaoui@email.com");
        clientDTO.setTelephone("0612345678");
        clientDTO.setAdresse("123 Rue Hassan II, Casablanca");
    }

    @Test
    void createClient_success() throws Exception {
        when(clientService.createClient(any(ClientExpediteurDTO.class))).thenReturn(clientDTO);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Client créé avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Alaoui"))
                .andExpect(jsonPath("$.data.prenom").value("Mohammed"))
                .andExpect(jsonPath("$.data.email").value("mohammed.alaoui@email.com"))
                .andExpect(jsonPath("$.data.telephone").value("0612345678"));

        verify(clientService, times(1)).createClient(any(ClientExpediteurDTO.class));
    }

    @Test
    void getAllClients_success() throws Exception {
        Page<ClientExpediteurDTO> page = new PageImpl<>(List.of(clientDTO));
        when(clientService.getAllClients(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Liste des clients récupérée avec succès"))
                .andExpect(jsonPath("$.data.content[0].nom").value("Alaoui"))
                .andExpect(jsonPath("$.data.content[0].prenom").value("Mohammed"));

        verify(clientService, times(1)).getAllClients(any(Pageable.class));
    }

    @Test
    void getClientById_found() throws Exception {
        when(clientService.getClientById("client1")).thenReturn(clientDTO);

        mockMvc.perform(get("/api/clients/client1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Client trouvé"))
                .andExpect(jsonPath("$.data.nom").value("Alaoui"))
                .andExpect(jsonPath("$.data.email").value("mohammed.alaoui@email.com"));

        verify(clientService, times(1)).getClientById("client1");
    }

    @Test
    void getClientById_notFound() throws Exception {
        when(clientService.getClientById("client999")).thenReturn(null);

        mockMvc.perform(get("/api/clients/client999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Client non trouvé"));

        verify(clientService, times(1)).getClientById("client999");
    }

    @Test
    void updateClient_success() throws Exception {
        ClientExpediteurDTO updatedClient = new ClientExpediteurDTO();
        updatedClient.setNom("Benani");
        updatedClient.setPrenom("Fatima");
        updatedClient.setEmail("fatima.benani@email.com");
        updatedClient.setTelephone("0687654321");
        updatedClient.setAdresse("456 Avenue Mohammed V, Rabat");

        when(clientService.updateClient(eq("client1"), any(ClientExpediteurDTO.class))).thenReturn(updatedClient);

        mockMvc.perform(put("/api/clients/client1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Client mis à jour avec succès"))
                .andExpect(jsonPath("$.data.nom").value("Benani"))
                .andExpect(jsonPath("$.data.prenom").value("Fatima"))
                .andExpect(jsonPath("$.data.email").value("fatima.benani@email.com"));

        verify(clientService, times(1)).updateClient(eq("client1"), any(ClientExpediteurDTO.class));
    }

    @Test
    void deleteClient_success() throws Exception {
        doNothing().when(clientService).deleteClient("client1");

        mockMvc.perform(delete("/api/clients/client1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Clinet supprimé avec succès"));

        verify(clientService, times(1)).deleteClient("client1");
    }

    @Test
    void searchClients_withKeyword_success() throws Exception {
        Page<ClientExpediteurDTO> page = new PageImpl<>(List.of(clientDTO));
        when(clientService.searchClients(eq("Mohammed"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/clients/search")
                        .param("keyword", "Mohammed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Résultats de recherche pour : Mohammed"))
                .andExpect(jsonPath("$.data.content[0].prenom").value("Mohammed"));

        verify(clientService, times(1)).searchClients(eq("Mohammed"), any(Pageable.class));
    }

    @Test
    void searchClients_withoutKeyword_success() throws Exception {
        Page<ClientExpediteurDTO> page = new PageImpl<>(List.of(clientDTO));
        when(clientService.searchClients(isNull(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/clients/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tous les clients ont été récupérés"));

        verify(clientService, times(1)).searchClients(isNull(), any(Pageable.class));
    }
}

