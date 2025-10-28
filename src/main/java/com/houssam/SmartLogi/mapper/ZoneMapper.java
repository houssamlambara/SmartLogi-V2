package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ZoneDTO;
import com.houssam.SmartLogi.model.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ZoneMapper {
    ZoneMapper INSTANCE = Mappers.getMapper(ZoneMapper.class);

    ZoneDTO toDTO(Zone entity);
    Zone toEntity(ZoneDTO dto);
}
