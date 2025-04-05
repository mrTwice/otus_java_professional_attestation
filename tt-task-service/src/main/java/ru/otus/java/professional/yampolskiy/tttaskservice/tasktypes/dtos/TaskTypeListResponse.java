package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos;

import java.util.List;

public class TaskTypeListResponse {
    // Если API возвращает не просто List<TaskTypeResponse>, а обёртку (например, с пагинацией или мета-инфой):
    private List<TaskTypeResponse> items;
    private int total;
}
