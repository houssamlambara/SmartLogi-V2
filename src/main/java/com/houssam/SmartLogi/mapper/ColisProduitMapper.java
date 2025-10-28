package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ColisProduitDTO;
import com.houssam.SmartLogi.model.ColisProduit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ColisProduitMapper {
    ColisProduitMapper INSTANCE = Mappers.getMapper(ColisProduitMapper.class);

    @Mapping(source = "colis.id", target = "colisId")
    @Mapping(source = "produit.id", target = "produitId")
    ColisProduitDTO toDTO(ColisProduit entity);

    @Mapping(source = "colisId", target = "colis.id")
    @Mapping(source = "produitId", target = "produit.id")
    ColisProduit toEntity(ColisProduitDTO dto);
}
