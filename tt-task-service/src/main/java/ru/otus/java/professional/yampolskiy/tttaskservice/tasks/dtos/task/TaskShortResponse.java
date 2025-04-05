package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskShortResponse {
    private UUID id;
    private String title;
    private String statusCode;
    private String typeCode;
}