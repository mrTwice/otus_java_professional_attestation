package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusResponse {
    private UUID id;
    private String code;
    private String name;
    private String description;

    private boolean isFinal;
    private boolean isDefault;

    private Integer sortOrder;
    private String color;

    private Instant createdAt;
    private Instant updatedAt;
}
