package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
