package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.dtos.*;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.entities.TaskType;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.mappers.TaskTypeMapper;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.services.TaskTypeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task-types")
@RequiredArgsConstructor
@Validated
public class TaskTypeController {

    private final TaskTypeService taskTypeService;
    private final TaskTypeMapper taskTypeMapper;

    @GetMapping
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public TaskTypeListResponse getAll() {
        List<TaskType> types = taskTypeService.findAllSorted();
        List<TaskTypeResponse> responses = taskTypeMapper.toResponseList(types);
        return new TaskTypeListResponse(responses, responses.size());
    }

    @GetMapping("/default")
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public List<TaskTypeShortResponse> getDefaultTypes() {
        return taskTypeMapper.toShortResponseList(taskTypeService.findDefaults());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public TaskTypeResponse getById(@PathVariable UUID id) {
        TaskType type = taskTypeService.findById(id);
        return taskTypeMapper.toResponse(type);
    }

    @GetMapping("/by-code/{code}")
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public TaskTypeResponse getByCode(@PathVariable String code) {
        TaskType type = taskTypeService.findByCode(code);
        return taskTypeMapper.toResponse(type);
    }

    @GetMapping("/search")
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public Page<TaskTypeResponse> search(@RequestParam String q, Pageable pageable) {
        return taskTypeService.search(q, pageable).map(taskTypeMapper::toResponse);
    }

    @PostMapping
    @PreAuthorize("@taskTypeAccessPolicy.canCreateTaskType(authentication)")
    public TaskTypeResponse create(@RequestBody @Valid TaskTypeCreateRequest request) {
        TaskType entity = taskTypeMapper.toEntity(request);
        TaskType created = taskTypeService.create(entity);
        return taskTypeMapper.toResponse(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@taskTypeAccessPolicy.canUpdateTaskType(authentication)")
    public TaskTypeResponse update(@PathVariable UUID id,
                                   @RequestBody @Valid TaskTypeUpdateRequest request) {
        TaskType existing = taskTypeService.findById(id);
        taskTypeMapper.updateEntity(existing, request);
        TaskType updated = taskTypeService.update(id, existing);
        return taskTypeMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@taskTypeAccessPolicy.canDeleteTaskType(authentication)")
    public void delete(@PathVariable UUID id) {
        taskTypeService.delete(id);
    }
}
