package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPriorityShortResponse {
    private UUID id;
    private String code;
    private String name;
}
