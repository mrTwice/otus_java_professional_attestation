package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.dtos.task.TaskFilterRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.Task;

import java.util.UUID;
import java.util.List;

public interface TaskService {

    Task create(Task task);

    Task update(UUID taskId, Task updatedTask);

    Task findById(UUID id);

    List<Task> findAll(TaskFilterRequest filter);

    List<Task> findAll();

    Page<Task> findAll(TaskFilterRequest filter, Pageable pageable);

    void delete(UUID taskId);

    List<Task> findSubtasks(UUID parentId);

    List<Task> findByAssignee(UUID assigneeId);

    List<Task> findByCreator(UUID creatorId);

    long countByAssignee(UUID assigneeId);
}
