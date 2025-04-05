package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTypeCreateRequest {

    //code задаётся при создании и не редактируется в будущем
    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    private boolean isDefault = false;

    private Integer sortOrder;

    private String icon;
}