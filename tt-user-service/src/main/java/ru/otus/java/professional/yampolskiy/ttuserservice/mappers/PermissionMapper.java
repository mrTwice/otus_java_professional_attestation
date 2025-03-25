package ru.otus.java.professional.yampolskiy.ttuserservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.permissions.PermissionDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.permissions.PermissionResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Permission;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {

    @Mapping(target = "id", ignore = true)
    Permission toEntityFromPermissionDTO(PermissionDTO dto);

    PermissionResponseDTO toResponseDTOFromEntity(Permission entity);

    Permission toEntityFromResponseDTO(PermissionResponseDTO dto);
}