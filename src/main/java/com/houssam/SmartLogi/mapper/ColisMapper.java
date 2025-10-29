package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.model.Colis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ColisMapper {

    ColisDTO toDTO(Colis colis);

    @Mapping(target = "livreur", ignore = true)
    @Mapping(target = "clientExpediteur", ignore = true)
    @Mapping(target = "destinataire", ignore = true)
    @Mapping(target = "zone", ignore = true)
    Colis toEntity(ColisDTO dto);
}
