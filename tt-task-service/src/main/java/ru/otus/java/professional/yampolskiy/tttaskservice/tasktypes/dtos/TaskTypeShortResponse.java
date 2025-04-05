package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTypeShortResponse {
    // возможно пригшодится для упрощенного вывода в виде списка
    private UUID id;
    private String code;
    private String name;
}
