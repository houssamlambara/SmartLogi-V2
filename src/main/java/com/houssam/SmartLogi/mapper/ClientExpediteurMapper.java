package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.model.ClientExpediteur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientExpediteurMapper {

    ClientExpediteurDTO toDTO(ClientExpediteur entity);
    ClientExpediteur toEntity(ClientExpediteurDTO dto);
}
