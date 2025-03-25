package ru.otus.java.professional.yampolskiy.ttuserservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user.UserUpdateDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user.UserCreateDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user.UserResponseDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.professional.yampolskiy.ttuserservice.mappers.UserMapper;
import ru.otus.java.professional.yampolskiy.ttuserservice.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


}
