package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ZoneDTO;
import com.houssam.SmartLogi.mapper.ZoneMapper;
import com.houssam.SmartLogi.model.Zone;
import com.houssam.SmartLogi.repository.ZoneRepository;
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
public class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private ZoneMapper mapper;

    @InjectMocks
    private ZoneService zoneService;

    @Test
    void createZone_success(){
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setNom("Casablanca");
        zoneDTO.setCodePostal("20000");

        Zone entity = new Zone();
        Zone savedEntity = entity;
        ZoneDTO resultDTO = new ZoneDTO();

        when(mapper.toEntity(zoneDTO)).thenReturn(entity);
        when(zoneRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(resultDTO);

        ZoneDTO result = zoneService.createZone(zoneDTO);

        assertNotNull(result);
        verify(zoneRepository, times(1)).save(entity);
        verify(mapper, times(1)).toDTO(savedEntity);
    }

    @Test
    void getZoneById_success(){
        Zone entity = new Zone();
        ZoneDTO zoneDTO = new ZoneDTO();

        when(zoneRepository.findById("zone1")).thenReturn(Optional.of(entity));
        when(mapper.toDTO(entity)).thenReturn(zoneDTO);

        ZoneDTO result = zoneService.getZoneById("zone1");

        assertNotNull(result);
        verify(zoneRepository, times(1)).findById("zone1");
        verify(mapper, times(1)).toDTO(entity);
    }

    @Test
    void getZoneById_notFound(){
        when(zoneRepository.findById("zone11")).thenReturn(Optional.empty());

        ZoneDTO result = zoneService.getZoneById("zone11");

        assertNull(result);
        verify(zoneRepository, times(1)).findById("zone11");
    }

    @Test
    void getAllZones_success(){
        Zone entity = new Zone();
        ZoneDTO zoneDTO = new ZoneDTO();
        Page<Zone> page = new PageImpl<>(List.of(entity));

        when(zoneRepository.findAll(PageRequest.of(0,10))).thenReturn(page);
        when(mapper.toDTO(entity)).thenReturn(zoneDTO);

        Page<ZoneDTO> result = zoneService.getAllZones(PageRequest.of(0,10));

        assertEquals(1, result.getContent().size());
        verify(zoneRepository, times(1)).findAll(PageRequest.of(0,10));
        verify(mapper, times(1)).toDTO(entity);
    }

    @Test
    void deleteZone_success() {
        doNothing().when(zoneRepository).deleteById("zone1");

        zoneService.deleteZone("zone1");

        verify(zoneRepository, times(1)).deleteById("zone1");
    }

}
