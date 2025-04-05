package ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.java.professional.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, UUID> {

    // Поиск по коду (как уникальному ключу)
    Optional<TaskStatus> findByCode(String code);

    // Все статусы отсортированные (например, для дропдауна)
    List<TaskStatus> findAllByOrderBySortOrderAsc();

    // Все статусы, где isFinal = true/false
    List<TaskStatus> findByIsFinal(boolean isFinal);

    // Все статусы, где isDefault = true/false
    List<TaskStatus> findByIsDefault(boolean isDefault);

    // Пагинация и фильтрация по имени (если нужно будет UI-поиск)
    @Query("""
        SELECT s FROM TaskStatus s
        WHERE (:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))
        ORDER BY s.sortOrder ASC
    """)
    Page<TaskStatus> searchByName(
            @Param("search") String search,
            Pageable pageable
    );
}