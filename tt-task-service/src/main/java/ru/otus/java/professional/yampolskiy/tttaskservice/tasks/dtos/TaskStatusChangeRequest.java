package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class TaskStatusChangeRequest {
    @NotNull
    private UUID statusId;
}