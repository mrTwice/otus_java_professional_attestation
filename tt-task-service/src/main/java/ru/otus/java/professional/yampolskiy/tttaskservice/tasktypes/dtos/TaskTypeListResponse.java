package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTypeListResponse {
    // Если API возвращает не просто List<TaskTypeResponse>, а обёртку (например, с пагинацией или мета-инфой):
    private List<TaskTypeResponse> items;
    private int total;
}
