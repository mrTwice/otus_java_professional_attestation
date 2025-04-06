package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilterRequest {
    //TODO добавить что-то для сортировки
    private UUID creatorId;
    private UUID assigneeId;
    private String statusCode;
    private String typeCode;
    private Instant createdAfter;
    private Instant createdBefore;
    private UUID priorityId;
}
