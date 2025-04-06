package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusCreateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusShortResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusUpdateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TaskStatusMapper {

    TaskStatus toEntity(TaskStatusCreateRequest request);

    void updateEntity(@MappingTarget TaskStatus existing, TaskStatusUpdateRequest update);

    TaskStatusResponse toResponse(TaskStatus entity);

    TaskStatusShortResponse toShortResponse(TaskStatus entity);

    List<TaskStatusResponse> toResponseList(List<TaskStatus> entities);

    List<TaskStatusShortResponse> toShortResponseList(List<TaskStatus> entities);
}
