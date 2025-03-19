package ru.otus.java.professional.yampolskiy.ttuserservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.RoleDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.RoleResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    RoleResponseDTO toResponseDTO(Role role);
}
