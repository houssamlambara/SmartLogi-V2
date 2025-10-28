package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.model.Livreur;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LivreurMapper {
    LivreurMapper INSTANCE = Mappers.getMapper(LivreurMapper.class);

    LivreurDTO toDTO(Livreur entity);
    Livreur toEntity(LivreurDTO dto);
}
