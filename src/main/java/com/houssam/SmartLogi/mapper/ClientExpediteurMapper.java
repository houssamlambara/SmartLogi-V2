package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ClientExpediteurDTO;
import com.houssam.SmartLogi.model.ClientExpediteur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientExpediteurMapper {

    @Mapping(source = "user.email", target = "email")
    ClientExpediteurDTO toDTO(ClientExpediteur entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "colis", ignore = true)
    ClientExpediteur toEntity(ClientExpediteurDTO dto);
}
