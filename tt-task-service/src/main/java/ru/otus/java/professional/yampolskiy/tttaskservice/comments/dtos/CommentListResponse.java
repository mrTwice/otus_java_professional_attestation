package ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponse {
    // единый объект на вывод, если потребуется
    private UUID taskId;
    private List<CommentResponse> comments;
}