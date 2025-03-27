package ru.otus.java.professional.yampolskiy.ttuserservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.role.RoleDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.role.RoleResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PermissionMapper.class)
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Role toEntityFromRoleDTO(RoleDTO dto);

    RoleResponseDTO toResponseDTOFromRole(Role entity);

    Role toEntityFromResponseDTO(RoleResponseDTO dto);
}

