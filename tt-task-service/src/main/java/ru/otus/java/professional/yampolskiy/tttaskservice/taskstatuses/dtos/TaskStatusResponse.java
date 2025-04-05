package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos;

import java.time.Instant;
import java.util.UUID;

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
