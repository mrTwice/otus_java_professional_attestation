package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityCreateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityShortResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityUpdateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.TaskPriority;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TaskPriorityMapper {

    TaskPriority toEntity(TaskPriorityCreateRequest request);

    void updateEntity(@MappingTarget TaskPriority existing, TaskPriorityUpdateRequest update);

    TaskPriorityResponse toResponse(TaskPriority priority);


    TaskPriorityShortResponse toShortResponse(TaskPriority priority);

    List<TaskPriorityResponse> toResponseList(List<TaskPriority> priorities);

    List<TaskPriorityShortResponse> toShortResponseList(List<TaskPriority> priorities);
}

