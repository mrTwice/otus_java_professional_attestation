package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.controllers;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.dtos.*;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.mappers.TaskStatusMapper;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.services.TaskStatusService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task-statuses")
@RequiredArgsConstructor
@Validated
public class TaskStatusController {

    private final TaskStatusService taskStatusService;
    private final TaskStatusMapper taskStatusMapper;

    @GetMapping
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public TaskStatusListResponse getAll() {
        List<TaskStatus> statuses = taskStatusService.findAllSorted();
        List<TaskStatusResponse> responses = taskStatusMapper.toResponseList(statuses);
        return new TaskStatusListResponse(responses, responses.size());
    }

    @GetMapping("/default")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public List<TaskStatusShortResponse> getDefaultStatuses() {
        return taskStatusMapper.toShortResponseList(taskStatusService.findDefaults());
    }

    @GetMapping("/final")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public List<TaskStatusShortResponse> getFinalStatuses(@RequestParam(defaultValue = "true") boolean isFinal) {
        return taskStatusMapper.toShortResponseList(taskStatusService.findFinal(isFinal));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public TaskStatusResponse getById(@PathVariable UUID id) {
        TaskStatus status = taskStatusService.findById(id);
        return taskStatusMapper.toResponse(status);
    }

    @GetMapping("/by-code/{code}")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public TaskStatusResponse getByCode(@PathVariable String code) {
        TaskStatus status = taskStatusService.findByCode(code);
        return taskStatusMapper.toResponse(status);
    }

    @GetMapping("/search")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public Page<TaskStatusResponse> search(@RequestParam String q, Pageable pageable) {
        return taskStatusService.search(q, pageable).map(taskStatusMapper::toResponse);
    }

    @PostMapping
    @PreAuthorize("@taskStatusAccessPolicy.canCreateTaskStatus(authentication)")
    public TaskStatusResponse create(@RequestBody @Valid TaskStatusCreateRequest request) {
        TaskStatus entity = taskStatusMapper.toEntity(request);
        TaskStatus created = taskStatusService.create(entity);
        return taskStatusMapper.toResponse(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@taskStatusAccessPolicy.canUpdateTaskStatus(authentication)")
    public TaskStatusResponse update(@PathVariable UUID id,
                                     @RequestBody @Valid TaskStatusUpdateRequest request) {
        TaskStatus existing = taskStatusService.findById(id);
        taskStatusMapper.updateEntity(existing, request);
        TaskStatus updated = taskStatusService.update(id, existing);
        return taskStatusMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@taskStatusAccessPolicy.canDeleteTaskStatus(authentication)")
    public void delete(@PathVariable UUID id) {
        taskStatusService.delete(id);
    }
}
