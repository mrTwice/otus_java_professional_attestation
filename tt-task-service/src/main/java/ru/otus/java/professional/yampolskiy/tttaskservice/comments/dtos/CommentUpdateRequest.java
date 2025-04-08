package ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление комментария")
public class CommentUpdateRequest {

    @NotBlank
    @Size(max = 5000)
    @Schema(description = "Новый текст комментария", example = "Обновлённый комментарий")
    private String content;
}

