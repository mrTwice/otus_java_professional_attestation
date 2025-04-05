package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public class TaskUpdateRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private UUID typeId;

    @NotNull
    private UUID statusId;

    private UUID assigneeId;
    private Instant dueDate;
    private Instant completedAt;

    private UUID parentId;
    private UUID queueId;
}
