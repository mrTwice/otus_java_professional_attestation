package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusChangeRequest {
    @NotNull
    private UUID statusId;
}