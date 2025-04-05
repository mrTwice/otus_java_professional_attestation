package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos;

import jakarta.validation.constraints.NotBlank;

public class TaskStatusUpdateRequest {
    // code и id — не изменяемы, задаются при создании и остаются постоянными ключами
    @NotBlank
    private String name;

    private String description;

    private boolean isFinal;
    private boolean isDefault;

    private Integer sortOrder;
    private String color;
}