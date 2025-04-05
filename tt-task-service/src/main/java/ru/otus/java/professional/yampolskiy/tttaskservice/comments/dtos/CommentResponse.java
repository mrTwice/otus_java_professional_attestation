package ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos;

import java.time.Instant;
import java.util.UUID;

public class CommentResponse {
    //Можно расширить  данными об авторе (authorName, avatarUrl), если связывать с user-сервисом.
    private UUID id;
    private UUID taskId;
    private UUID authorId;

    private String content;

    private Instant createdAt;
    private Instant updatedAt;
}