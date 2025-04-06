package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeCreateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeShortResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeUpdateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.entities.TaskType;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TaskTypeMapper {

    TaskType toEntity(TaskTypeCreateRequest request);

    void updateEntity(@MappingTarget TaskType target, TaskTypeUpdateRequest update);

    TaskTypeResponse toResponse(TaskType type);

    TaskTypeShortResponse toShortResponse(TaskType type);

    List<TaskTypeResponse> toResponseList(List<TaskType> types);

    List<TaskTypeShortResponse> toShortResponseList(List<TaskType> types);
}
