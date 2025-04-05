package ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequest {
    // Используется только если разрешено редактировать комментарии.
    @NotBlank
    @Size(max = 5000)
    private String content;
}
