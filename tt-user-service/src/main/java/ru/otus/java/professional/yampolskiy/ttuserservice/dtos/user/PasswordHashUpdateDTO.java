package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordHashUpdateDTO {
    @NotBlank
    private String passwordHash;
}