package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.mapper.ClientExpediteurMapper;
import com.houssam.SmartLogi.model.ClientExpediteur;
import com.houssam.SmartLogi.repository.ClientExpediteurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class ClientExpediteurServiceTest {

    @InjectMocks
    private ClientExpediteurService clientExpediteurService;

    @Mock
    private ClientExpediteurRepository clientExpediteurRepository;

    @Mock
    private ClientExpediteurMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createClientExpediteur_success() {
        ClientExpediteurDTO dto = new ClientExpediteurDTO();
        dto.setNom("Houssam");
        ClientExpediteur entity = new ClientExpediteur();
        entity.setNom("Houssam");
        ClientExpediteur savedEntity = new ClientExpediteur();
        savedEntity.setNom("Houssam");
        ClientExpediteurDTO resultDTO = new ClientExpediteurDTO();
        resultDTO.setNom("Houssam");

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(clientExpediteurRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(resultDTO);

        // Appel du service
        ClientExpediteurDTO result = clientExpediteurService.createClient(dto);

        // Vérifications
        assertNotNull(result);
        assertEquals("Houssam", result.getNom());
        verify(clientExpediteurRepository, times(1)).save(entity);
        verify(mapper, times(1)).toDTO(savedEntity);
    }

    @Test
    void getAllClients_success(){
        ClientExpediteur clientExpediteur = new ClientExpediteur();
        Page<ClientExpediteur> page = new PageImpl<>(List.of(clientExpediteur));
        ClientExpediteurDTO clientExpediteurDTO = new ClientExpediteurDTO();

        when(clientExpediteurRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(mapper.toDTO(clientExpediteur)).thenReturn(clientExpediteurDTO);

        Page<ClientExpediteurDTO> result = clientExpediteurService.getAllClients(PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        verify(mapper, times(1)).toDTO(clientExpediteur);
    }

    @Test
    void getClientById_found(){
        ClientExpediteur entity = new ClientExpediteur();
        ClientExpediteurDTO dto = new ClientExpediteurDTO();

        when(clientExpediteurRepository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        ClientExpediteurDTO result = clientExpediteurService.getClientById("1");

        assertNotNull(result);
        verify(mapper, times(1)).toDTO(entity);
    }

    @Test
    void getClientById_notFound(){
        when(clientExpediteurRepository.findById("1")).thenReturn(Optional.empty());
        ClientExpediteurDTO result = clientExpediteurService.getClientById("1");
        assertNull(result);
    }

    @Test
    void updateClient_success(){
        ClientExpediteur entity =  new ClientExpediteur();
        ClientExpediteurDTO dto = new ClientExpediteurDTO();
        dto.setNom("Houssam");
        ClientExpediteur updatedEntity = new ClientExpediteur();

        when(clientExpediteurRepository.findById("1")).thenReturn(Optional.of(entity));
        when(clientExpediteurRepository.save(entity)).thenReturn(updatedEntity);
        when(mapper.toDTO(updatedEntity)).thenReturn(dto);

        ClientExpediteurDTO result = clientExpediteurService.updateClient("1", dto);

        assertEquals("Houssam", result.getNom());
        verify(clientExpediteurRepository, times(1)).findById("1");
    }

    @Test
    void updateClient_notFound() {
        ClientExpediteurDTO dto = new ClientExpediteurDTO();
        when(clientExpediteurRepository.findById("id1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> clientExpediteurService.updateClient("id1", dto));
    }

    @Test
    void deleteClient_success(){
        doNothing().when(clientExpediteurRepository).deleteById("1");
        clientExpediteurService.deleteClient("1");
        verify(clientExpediteurRepository, times(1)).deleteById("1");
    }

    @Test
    void searchClients_withKeyword(){
        String keyword = "hou";
        ClientExpediteur entity = new ClientExpediteur();
        ClientExpediteurDTO dto = new ClientExpediteurDTO();
        Page<ClientExpediteur> page = new PageImpl<>(List.of(entity));

        when(clientExpediteurRepository.searchClients(keyword, PageRequest.of(0, 10))).thenReturn(page);
        when(mapper.toDTO(entity)).thenReturn(dto);

        Page<ClientExpediteurDTO> result = clientExpediteurService.searchClients(keyword, PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
        verify(mapper, times(1)).toDTO(entity);
    }

    @Test
    void sercheClients_noKeyword(){
        ClientExpediteur entity = new ClientExpediteur();
        ClientExpediteurDTO dto = new ClientExpediteurDTO();
        Page<ClientExpediteur> page = new PageImpl<>(List.of(entity));

        when(clientExpediteurRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(mapper.toDTO(entity)).thenReturn(dto);

        Page<ClientExpediteurDTO> result = clientExpediteurService.searchClients("", PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        verify(mapper, times(1)).toDTO(entity);
    }

}
