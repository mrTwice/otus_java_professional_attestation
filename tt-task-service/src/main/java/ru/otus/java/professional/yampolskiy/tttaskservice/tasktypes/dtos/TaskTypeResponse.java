package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos;

import java.time.Instant;
import java.util.UUID;

public class TaskTypeResponse {
    private UUID id;
    private String code;
    private String name;
    private String description;

    private boolean isDefault;
    private Integer sortOrder;
    private String icon;

    private Instant createdAt;
    private Instant updatedAt;
}
