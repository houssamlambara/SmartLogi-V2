package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.model.Livreur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LivreurMapper {

    LivreurDTO toDTO(Livreur entity);
    Livreur toEntity(LivreurDTO dto);
}
