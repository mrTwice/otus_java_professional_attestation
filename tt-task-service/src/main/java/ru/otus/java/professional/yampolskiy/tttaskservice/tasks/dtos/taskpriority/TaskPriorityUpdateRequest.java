package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskPriorityUpdateRequest {

    @NotBlank
    private String name;

    private String description;
    private Integer sortOrder;
    private String color;
    private boolean isDefault;
}
