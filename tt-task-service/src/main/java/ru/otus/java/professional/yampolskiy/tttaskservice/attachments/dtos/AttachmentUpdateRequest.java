package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление вложения")
public class AttachmentUpdateRequest {

    @NotBlank
    @Schema(description = "Новое имя файла", example = "new-design.pdf")
    private String fileName;
}
