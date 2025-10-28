package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.model.Colis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ColisMapper {

    @Mapping(source = "livreur.id", target = "livreurId")
    @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(source = "zone.id", target = "zoneId")
    ColisDTO toDTO(Colis colis);

    @Mapping(source = "livreurId", target = "livreur.id")
    @Mapping(source = "clientExpediteurId", target = "clientExpediteur.id")
    @Mapping(source = "destinataireId", target = "destinataire.id")
    @Mapping(source = "zoneId", target = "zone.id")
    Colis toEntity(ColisDTO dto);
}
