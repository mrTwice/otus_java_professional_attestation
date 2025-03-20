package ru.otus.java.professional.yampolskiy.ttuserservice.services;

import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    Role createRole(Role role);

    Role getRoleById(Long id);

    Role getRoleByName(String name);

    List<Role> getAllRoles();

    Role updateRole(Long id, Role role);

    void deleteRole(Long id);

    Set<Role> validateRoles(Set<Role> roles);
}
