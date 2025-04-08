package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO для обновления пароля пользователя (захешированное значение)")
public class PasswordHashUpdateDTO {

    @NotBlank
    @Schema(description = "Хешированный пароль", example = "$2a$12$...")
    private String passwordHash;
}
