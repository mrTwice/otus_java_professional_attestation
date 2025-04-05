package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
