package ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos;

import java.util.List;
import java.util.UUID;

public class CommentListResponse {
    // единый объект на вывод, если потребуется
    private UUID taskId;
    private List<CommentResponse> comments;
}