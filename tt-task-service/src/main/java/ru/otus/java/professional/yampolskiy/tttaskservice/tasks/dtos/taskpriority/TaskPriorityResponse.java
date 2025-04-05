package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPriorityResponse {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private Integer sortOrder;
    private String color;
    private boolean isDefault;
    private Instant createdAt;
    private Instant updatedAt;
}
