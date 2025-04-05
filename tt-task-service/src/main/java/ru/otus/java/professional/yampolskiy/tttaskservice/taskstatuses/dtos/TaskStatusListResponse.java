package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos;

import java.util.List;

public class TaskStatusListResponse {
    // для пагинации, фильтрации
    private List<TaskStatusResponse> items;
    private int total;
}
