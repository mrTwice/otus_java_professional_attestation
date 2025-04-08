package ru.otus.java.professional.yampolskiy.ttuserservice.dtos.permissions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO для создания или обновления разрешения")
public class PermissionDTO {

    @NotBlank
    @Schema(description = "Уникальное имя разрешения", example = "ROLE_MANAGE_USERS")
    private String name;

    @NotBlank
    @Schema(description = "Описание разрешения", example = "Позволяет управлять пользователями")
    private String description;
}
