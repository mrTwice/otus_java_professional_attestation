package ru.otus.java.professional.yampolskiy.ttuserservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private Long id;
    private String name;
}