package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.DestinataireDTO;
import com.houssam.SmartLogi.model.Destinataire;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DestinataireMapper {
    DestinataireMapper INSTANCE = Mappers.getMapper(DestinataireMapper.class);

    DestinataireDTO toDTO(Destinataire entity);
    Destinataire toEntity(DestinataireDTO dto);
}
