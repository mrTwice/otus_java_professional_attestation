package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityCreateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityShortResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityUpdateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.TaskPriority;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.mappers.TaskPriorityMapper;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.services.TaskPriorityService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task-priorities")
@RequiredArgsConstructor
@Validated
public class TaskPriorityController {

    private final TaskPriorityService taskPriorityService;
    private final TaskPriorityMapper taskPriorityMapper;

    @GetMapping
    @PreAuthorize("@taskPriorityAccessPolicy.canViewTaskPriority(authentication)")
    public List<TaskPriorityResponse> getAll() {
        List<TaskPriority> priorities = taskPriorityService.findAllSorted();
        return taskPriorityMapper.toResponseList(priorities);
    }

    @GetMapping("/default")
    @PreAuthorize("@taskPriorityAccessPolicy.canViewTaskPriority(authentication)")
    public List<TaskPriorityShortResponse> getDefaults() {
        return taskPriorityMapper.toShortResponseList(taskPriorityService.findDefaults());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@taskPriorityAccessPolicy.canViewTaskPriority(authentication)")
    public TaskPriorityResponse getById(@PathVariable UUID id) {
        TaskPriority priority = taskPriorityService.findById(id);
        return taskPriorityMapper.toResponse(priority);
    }

    @GetMapping("/by-code/{code}")
    @PreAuthorize("@taskPriorityAccessPolicy.canViewTaskPriority(authentication)")
    public TaskPriorityResponse getByCode(@PathVariable String code) {
        TaskPriority priority = taskPriorityService.findByCode(code);
        return taskPriorityMapper.toResponse(priority);
    }

    @PostMapping
    @PreAuthorize("@taskPriorityAccessPolicy.canCreateTaskPriority(authentication)")
    public TaskPriorityResponse create(@RequestBody @Valid TaskPriorityCreateRequest request) {
        TaskPriority entity = taskPriorityMapper.toEntity(request);
        TaskPriority created = taskPriorityService.create(entity);
        return taskPriorityMapper.toResponse(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@taskPriorityAccessPolicy.canUpdateTaskPriority(authentication)")
    public TaskPriorityResponse update(@PathVariable UUID id,
                                       @RequestBody @Valid TaskPriorityUpdateRequest request) {
        TaskPriority existing = taskPriorityService.findById(id);
        taskPriorityMapper.updateEntity(existing, request);
        TaskPriority updated = taskPriorityService.update(id, existing);
        return taskPriorityMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@taskPriorityAccessPolicy.canDeleteTaskPriority(authentication)")
    public void delete(@PathVariable UUID id) {
        taskPriorityService.delete(id);
    }
}

