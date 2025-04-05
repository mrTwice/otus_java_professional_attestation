package ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CommentCreateRequest {
    @NotNull
    private UUID taskId; // authorId может подтягиваться с токена (а не передаётся клиентом), можно сделать его необязательным в DTO.

    @NotNull
    private UUID authorId;

    @NotBlank
    @Size(max = 5000)
    private String content;
}
