package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.model.ClientExpediteur;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientExpediteurMapper {
    ClientExpediteurMapper INSTANCE = Mappers.getMapper(ClientExpediteurMapper.class);

    ClientExpediteurDTO toDTO(ClientExpediteur entity);
    ClientExpediteur toEntity(ClientExpediteurDTO dto);
}
