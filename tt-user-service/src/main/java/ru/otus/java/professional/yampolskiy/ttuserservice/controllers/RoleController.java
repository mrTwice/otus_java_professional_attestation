package ru.otus.java.professional.yampolskiy.ttuserservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.role.RoleDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.role.RoleResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.RoleMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

}