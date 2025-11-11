package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.exception.ResourceNotFoundException;
import com.houssam.SmartLogi.mapper.LivreurMapper;
import com.houssam.SmartLogi.model.Livreur;
import com.houssam.SmartLogi.model.Zone;
import com.houssam.SmartLogi.repository.LivreurRepository;
import com.houssam.SmartLogi.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class LivreurServiceTest {

    @InjectMocks
    private LivreurService livreurService;

    @Mock
    private LivreurRepository livreurRepository;

    @Mock
    private LivreurMapper mapper;

    @Mock
    private ZoneRepository zoneRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLivreur_success() {
        LivreurDTO livreurDTO = new LivreurDTO();
        livreurDTO.setNom("Houssam");
        livreurDTO.setZoneAssigneeId("zone1");

        Livreur entity = new Livreur();
        Livreur SavedEntity = new Livreur();
        LivreurDTO resultDTO = new LivreurDTO();

        Zone zoneMock = new Zone();

        when(mapper.toEntity(livreurDTO)).thenReturn(entity);
        when(zoneRepository.findById("zone1")).thenReturn(Optional.of(zoneMock));
        when(livreurRepository.save(entity)).thenReturn(SavedEntity);
        when(mapper.toDTO(SavedEntity)).thenReturn(resultDTO);

        LivreurDTO result = livreurService.createLivreur(livreurDTO);

        assertNotNull(result);
        verify(livreurRepository,times(1)).save(entity);
        verify(zoneRepository,times(1)).findById("zone1");
        verify(mapper,times(1)).toDTO(SavedEntity);
    }

    @Test
    void createLivreur_zoneNotFound(){
        LivreurDTO livreurDTO = new LivreurDTO();
        livreurDTO.setZoneAssigneeId("zone1");

        when(zoneRepository.findById("zone1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, ()
                -> livreurService.createLivreur(livreurDTO));
    }

    @Test
    void getAllLivreurs_success(){
        Livreur livreur = new Livreur();
        Page<Livreur> page = new PageImpl<>(List.of(livreur));
        LivreurDTO resultDTO = new LivreurDTO();

        when(livreurRepository.findAll(PageRequest.of(0,10))).thenReturn(page);
        when(mapper.toDTO(livreur)).thenReturn(resultDTO);

        Page<LivreurDTO> result = livreurService.getAllLivreurs(PageRequest.of(0,10));
        assertEquals(1, result.getContent().size());
    }

    @Test
    void updateLivreur_success() {
        LivreurDTO livreurDTO = new LivreurDTO();
        livreurDTO.setNom("Hamid");
        livreurDTO.setZoneAssigneeId("zone1");

        Livreur livreur = new Livreur();
        Livreur updated = new Livreur();
        Zone zoneMock = new Zone();
        LivreurDTO resultDTO = new LivreurDTO();

        when(livreurRepository.findById("livreur1")).thenReturn(Optional.of(livreur));
        when(zoneRepository.findById("zone1")).thenReturn(Optional.of(zoneMock));
        when(livreurRepository.save(livreur)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(resultDTO);

        LivreurDTO result = livreurService.updateLivreur("livreur1", livreurDTO);
        assertNotNull(result);
        verify(livreurRepository, times(1)).save(livreur);
        verify(mapper, times(1)).toDTO(updated);
    }

    @Test
    void updateLivreur_notFound(){
        when(livreurRepository.findById("livreur1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                livreurService.updateLivreur("livreur1", new LivreurDTO()));
    }

    @Test
    void deleteLivreur_success(){
        doNothing().when(livreurRepository).deleteById("livreur1");
        livreurService.deleteLivreur("livreur1");
        verify(livreurRepository, times(1)).deleteById("livreur1");
    }

    @Test
    void searchLivreurs_withKyword(){
        Livreur livreur = new Livreur();
        LivreurDTO livreurDTO = new LivreurDTO();
        Page<Livreur> page = new PageImpl<>(List.of(livreur));

        when(livreurRepository.searchLivreurs("Houssam", PageRequest.of(0,10))).thenReturn(page);
        when(mapper.toDTO(livreur)).thenReturn(livreurDTO);

        Page<LivreurDTO> result = livreurService.searchLivreurs("Houssam", PageRequest.of(0,10));
        assertEquals(1, result.getContent().size());
    }

    @Test
    void searchLivreurs_noKeyword(){
        Livreur livreur = new Livreur();
        LivreurDTO livreurDTO = new LivreurDTO();
        Page<Livreur> page = new PageImpl<>(List.of(livreur));

        when(livreurRepository.findAll(PageRequest.of(0,10))).thenReturn(page);
        when(mapper.toDTO(livreur)).thenReturn(livreurDTO);

        Page<LivreurDTO> result = livreurService.searchLivreurs("", PageRequest.of(0,10));
        assertEquals(1, result.getContent().size());
    }
}


