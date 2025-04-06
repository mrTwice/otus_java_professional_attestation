package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.task.*;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.Task;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.mappers.TaskMapper;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.services.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    @PreAuthorize("@taskAccessPolicy.canCreateTask(authentication)")
    public TaskResponse create(@RequestBody @Valid TaskCreateRequest request) {
        Task task = taskMapper.toEntity(request);
        Task created = taskService.create(task);
        return taskMapper.toResponse(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@taskAccessPolicy.canUpdateTask(authentication)")
    public TaskResponse update(@PathVariable UUID id,
                               @RequestBody @Valid TaskUpdateRequest request) {
        Task existing = taskService.findById(id);
        taskMapper.updateEntity(existing, request);
        Task updated = taskService.update(id, existing);
        return taskMapper.toResponse(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public TaskResponse getById(@PathVariable UUID id) {
        return taskMapper.toResponse(taskService.findById(id));
    }

    @GetMapping("/{id}/short")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public TaskShortResponse getShort(@PathVariable UUID id) {
        return taskMapper.toShortResponse(taskService.findById(id));
    }

    @PostMapping("/filter")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public List<TaskShortResponse> filter(@RequestBody TaskFilterRequest filter) {
        return taskMapper.toShortResponseList(taskService.findAll(filter));
    }

    @PostMapping("/filter/paged")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public Page<TaskShortResponse> filterPaged(@RequestBody TaskFilterRequest filter, Pageable pageable) {
        return taskService.findAll(filter, pageable).map(taskMapper::toShortResponse);
    }

    @GetMapping("/subtasks/{parentId}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public List<TaskShortResponse> getSubtasks(@PathVariable UUID parentId) {
        return taskMapper.toShortResponseList(taskService.findSubtasks(parentId));
    }

    @GetMapping("/by-assignee/{userId}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public List<TaskShortResponse> getByAssignee(@PathVariable UUID userId) {
        return taskMapper.toShortResponseList(taskService.findByAssignee(userId));
    }

    @GetMapping("/by-creator/{userId}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public List<TaskShortResponse> getByCreator(@PathVariable UUID userId) {
        return taskMapper.toShortResponseList(taskService.findByCreator(userId));
    }

    @GetMapping("/count/by-assignee/{userId}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public long countByAssignee(@PathVariable UUID userId) {
        return taskService.countByAssignee(userId);
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("@taskAccessPolicy.canUpdateTask(authentication) or @taskAccessPolicy.canAssignTask(authentication)")
    public TaskResponse changeStatus(@PathVariable UUID id,
                                     @RequestBody @Valid TaskStatusChangeRequest request) {
        Task existing = taskService.findById(id);
        existing.setStatusId(request.getStatusId());
        return taskMapper.toResponse(taskService.update(id, existing));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@taskAccessPolicy.canDeleteTask(authentication)")
    public void delete(@PathVariable UUID id) {
        taskService.delete(id);
    }
}
