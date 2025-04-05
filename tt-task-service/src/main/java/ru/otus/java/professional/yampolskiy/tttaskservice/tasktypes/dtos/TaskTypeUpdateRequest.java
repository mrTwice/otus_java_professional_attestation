package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTypeUpdateRequest {
    // code и id не редактируются — они задаются 1 раз и потом используются как ключ.
    @NotBlank
    private String name;

    private String description;

    private boolean isDefault;

    private Integer sortOrder;

    private String icon;
}