package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.model.Produit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProduitMapper {

    ProduitDTO toDTO(Produit entity);
    Produit toEntity(ProduitDTO dto);
}
