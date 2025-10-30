package com.houssam.SmartLogi.mapper;

import com.houssam.SmartLogi.dto.LivreurDTO;
import com.houssam.SmartLogi.model.Livreur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LivreurMapper {

    @Mapping(source = "zoneAssignee.id", target = "zoneAssigneeId")
    LivreurDTO toDTO(Livreur entity);

    @Mapping(target = "zoneAssignee", ignore = true)
    @Mapping(target = "colis", ignore = true)
    Livreur toEntity(LivreurDTO dto);
}
