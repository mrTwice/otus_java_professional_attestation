package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.TaskPriority;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskPriorityRepository extends JpaRepository<TaskPriority, UUID> {
    Optional<TaskPriority> findByCode(String code);
    List<TaskPriority> findAllByOrderBySortOrderAsc();
    List<TaskPriority> findByIsDefaultTrue();
}