package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.ZoneDTO;
import com.houssam.SmartLogi.model.Zone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    ZoneDTO toDTO(Zone entity);
    Zone toEntity(ZoneDTO dto);
}
