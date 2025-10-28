package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.DestinataireDTO;
import com.houssam.SmartLogi.model.Destinataire;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DestinataireMapper {

    DestinataireDTO toDTO(Destinataire entity);
    Destinataire toEntity(DestinataireDTO dto);
}
