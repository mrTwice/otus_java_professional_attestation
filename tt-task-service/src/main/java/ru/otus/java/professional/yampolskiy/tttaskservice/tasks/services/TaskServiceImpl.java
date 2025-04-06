package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.task.TaskFilterRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.Task;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.TaskPriority;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.repositories.TaskRepository;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.services.filtering.TaskSpecifications;
import ru.otus.java.professional.yampolskiy.tttaskservice.common.DomainValidator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskPriorityService taskPriorityService;
    private final DomainValidator<Task> taskValidator;

    @Override
    public Task create(Task task) {
        taskValidator.validateForCreate(task);
        applyPriority(task);
        return taskRepository.save(task);
    }

    @Override
    public Task update(UUID taskId, Task updatedTask) {
        Task existingTask = getByIdOrThrow(taskId);
        taskValidator.validateForUpdate(existingTask, updatedTask);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatusId(updatedTask.getStatusId());
        existingTask.setStatusCode(updatedTask.getStatusCode());
        existingTask.setAssigneeId(updatedTask.getAssigneeId());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setCompletedAt(updatedTask.getCompletedAt());
        existingTask.setPriorityId(updatedTask.getPriorityId());
        existingTask.setTypeId(updatedTask.getTypeId());
        existingTask.setTypeCode(updatedTask.getTypeCode());

        applyPriority(existingTask);

        return taskRepository.save(existingTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Task findById(UUID id) {
        return getByIdOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll(TaskFilterRequest filter) {
        return taskRepository.findAll(TaskSpecifications.fromFilter(filter));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll(TaskSpecifications.isNotDeleted());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> findAll(TaskFilterRequest filter, Pageable pageable) {
        return taskRepository.findAll(TaskSpecifications.fromFilter(filter), pageable);
    }

    @Override
    public void delete(UUID taskId) {
        Task task = getByIdOrThrow(taskId);
        if (task.getDeletedAt() != null) {
            throw new IllegalStateException("Task already deleted");
        }
        task.setDeletedAt(Instant.now());
        taskRepository.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findSubtasks(UUID parentId) {
        return taskRepository.findByParent_Id(parentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByAssignee(UUID assigneeId) {
        return taskRepository.findByAssigneeIdAndDeletedAtIsNull(assigneeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByCreator(UUID creatorId) {
        return taskRepository.findByCreatorIdAndDeletedAtIsNull(creatorId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAssignee(UUID assigneeId) {
        return taskRepository.countByAssigneeIdAndDeletedAtIsNull(assigneeId);
    }

    private Task getByIdOrThrow(UUID id) {
        return taskRepository.findById(id)
                .filter(task -> task.getDeletedAt() == null)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    private void applyPriority(Task task) {
        if (task.getPriorityId() != null) {
            TaskPriority priority = taskPriorityService.findById(task.getPriorityId());
            task.setPriorityCode(priority.getCode());
        } else {
            task.setPriorityCode(null);
        }
    }
}
