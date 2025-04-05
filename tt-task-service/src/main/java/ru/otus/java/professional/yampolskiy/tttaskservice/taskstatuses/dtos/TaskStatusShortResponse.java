package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusShortResponse {
    // Может быть полезно для UI, когда нужен только статус в виде лейбла
    private UUID id;
    private String code;
    private String name;
    private String color;
}
