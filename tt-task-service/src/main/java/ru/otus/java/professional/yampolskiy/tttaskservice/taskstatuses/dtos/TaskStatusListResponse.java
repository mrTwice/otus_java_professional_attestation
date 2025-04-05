package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusListResponse {
    // для пагинации, фильтрации
    private List<TaskStatusResponse> items;
    private int total;
}
