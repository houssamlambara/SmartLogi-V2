package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.HistoriqueLivraisonDTO;
import com.houssam.SmartLogi.model.HistoriqueLivraison;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HistoriqueLivraisonMapper {
    HistoriqueLivraisonMapper INSTANCE = Mappers.getMapper(HistoriqueLivraisonMapper.class);

    @Mapping(source = "colis.id", target = "colisId")
    HistoriqueLivraisonDTO toDTO(HistoriqueLivraison entity);

    @Mapping(source = "colisId", target = "colis.id")
    HistoriqueLivraison toEntity(HistoriqueLivraisonDTO dto);
}
