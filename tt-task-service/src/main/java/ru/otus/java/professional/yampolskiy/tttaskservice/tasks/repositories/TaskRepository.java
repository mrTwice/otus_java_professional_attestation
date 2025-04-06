package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.Task;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    List<Task> findActiveTasksByStatusCodeAndAssigneeId(String statusCode, UUID assigneeId);

    long countByAssigneeIdAndDeletedAtIsNull(UUID assigneeId);

    List<Task> findByParent_Id(UUID parentId);

    List<Task> findByAssigneeIdAndDeletedAtIsNull(UUID assigneeId);

    List<Task> findByCreatorIdAndDeletedAtIsNull(UUID creatorId);

}
