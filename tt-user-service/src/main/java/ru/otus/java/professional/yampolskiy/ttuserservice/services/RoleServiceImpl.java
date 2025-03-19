package ru.otus.java.professional.yampolskiy.ttuserservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.exceptions.ResourceNotFoundException;
import ru.otus.java.professional.yampolskiy.ttuserservice.repositories.RoleRepository;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.CommonRoleValidator;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.RoleUniqueValidator;
import ru.otus.java.professional.yampolskiy.ttuserservice.validators.Validator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final Validator<Role> commonRoleValidator;
    private final Validator<Role> roleUniqueValidator;

    @Override
    public Role createRole(Role role) {
        commonRoleValidator.validate(role);
        roleUniqueValidator.validate(role);
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + name));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role updateRole(Long id, Role role) {
        commonRoleValidator.validate(role);
        Role existingRole = getRoleById(id);

        if (!existingRole.getName().equals(role.getName())) {
            roleUniqueValidator.validate(role);
        }

        existingRole.setName(role.getName());
        return roleRepository.save(existingRole);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }
}