package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusCreateRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    private boolean isFinal = false;
    private boolean isDefault = false;

    private Integer sortOrder;
    private String color; // напр. "#33AACC"
}