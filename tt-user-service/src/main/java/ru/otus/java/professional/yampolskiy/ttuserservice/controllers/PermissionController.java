package ru.otus.java.professional.yampolskiy.ttuserservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.PermissionMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.RoleMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.PermissionService;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

}