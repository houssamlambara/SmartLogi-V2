package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.DestinataireDTO;
import com.houssam.SmartLogi.mapper.DestinataireMapper;
import com.houssam.SmartLogi.model.Destinataire;
import com.houssam.SmartLogi.repository.DestinataireRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DestinataireServiceTest {

    @InjectMocks
    private  DestinataireService destinataireService;

    @Mock
    private DestinataireRepository destinataireRepository;

    @Mock
    private DestinataireMapper destinataireMapper;

    @Test
    void createDestinataire_success(){
        DestinataireDTO destinataireDTO = new DestinataireDTO();
        destinataireDTO.setNom("Houssam");
        destinataireDTO.setPrenom("Lambara");

        Destinataire entity = new Destinataire();
        Destinataire savedEntity = new Destinataire();
        DestinataireDTO resultDTO  = new DestinataireDTO();

        when(destinataireMapper.toEntity(destinataireDTO)).thenReturn(entity);
        when(destinataireRepository.save(entity)).thenReturn(savedEntity);
        when(destinataireMapper.toDTO(savedEntity)).thenReturn(resultDTO);

        DestinataireDTO result = destinataireService.createDestinataire(destinataireDTO);

        assertNotNull(result);
        verify(destinataireRepository, times(1)).save(entity);
        verify(destinataireMapper, times(1)).toDTO(savedEntity);
    }

    @Test
    void updateDestinataire_success(){
        String id ="1";
        DestinataireDTO destinataireDTO = new DestinataireDTO();
        Destinataire destinataire = new Destinataire();
        Destinataire updatedEntity = new Destinataire();

        destinataireDTO.setNom("Houssam");
        destinataireDTO.setPrenom("Lambara");
        destinataireDTO.setEmail("houssam@mail.com");
        updatedEntity.setId(id);

        DestinataireDTO resultDTO = new DestinataireDTO();

        when(destinataireRepository.findById(id)).thenReturn(Optional.of(destinataire));
        when(destinataireRepository.save(destinataire)).thenReturn(updatedEntity);
        when(destinataireMapper.toDTO(updatedEntity)).thenReturn(resultDTO);

        DestinataireDTO result = destinataireService.updateDestinataire(id, destinataireDTO);

        assertNotNull(result);
        verify(destinataireRepository, times(1)).findById(id);
        verify(destinataireRepository, times(1)).save(any(Destinataire.class));
        verify(destinataireMapper, times(1)).toDTO(updatedEntity);

    }





}
