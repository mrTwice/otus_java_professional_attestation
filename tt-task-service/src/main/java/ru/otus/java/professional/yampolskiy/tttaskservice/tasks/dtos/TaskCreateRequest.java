package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private UUID typeId;

    @NotNull
    private UUID statusId;

    @NotNull
    private UUID creatorId;

    private UUID assigneeId;
    private Instant dueDate;

    private UUID parentId;
    private UUID queueId;
}
