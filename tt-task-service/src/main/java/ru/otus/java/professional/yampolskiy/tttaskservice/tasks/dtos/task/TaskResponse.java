package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.Task;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;

    private UUID typeId;
    private String typeCode;

    private UUID statusId;
    private String statusCode;

    private UUID creatorId;
    private UUID assigneeId;
    private Instant dueDate;
    private Instant completedAt;
    private Instant deletedAt;

    private Instant createdAt;
    private Instant updatedAt;

    private UUID parentId;
    private UUID queueId;

    private List<Task> subtasks; // или List<UUID> если без вложенности
}