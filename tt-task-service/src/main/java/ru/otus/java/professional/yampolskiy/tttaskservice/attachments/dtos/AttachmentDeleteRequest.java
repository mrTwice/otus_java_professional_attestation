package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на удаление нескольких вложений")
public class AttachmentDeleteRequest {

    @NotEmpty
    @Schema(description = "Список ID вложений для удаления")
    private List<UUID> attachmentIds;
}

