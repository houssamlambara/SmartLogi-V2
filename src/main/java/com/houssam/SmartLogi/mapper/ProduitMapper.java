package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.model.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProduitMapper {
    ProduitMapper INSTANCE = Mappers.getMapper(ProduitMapper.class);

    ProduitDTO toDTO(Produit entity);
    Produit toEntity(ProduitDTO dto);
}
