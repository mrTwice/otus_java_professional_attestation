package ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasktypes.entities.TaskType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskTypeRepository extends JpaRepository<TaskType, UUID> {

    // Поиск по коду (уникальный, для обновлений и ссылок)
    Optional<TaskType> findByCode(String code);

    // Все типы задач отсортированные (для списков, селектов и т.п.)
    List<TaskType> findAllByOrderBySortOrderAsc();

    // Все типы, где isDefault = true (обычно будет один)
    List<TaskType> findByIsDefaultTrue();

    // Поиск с фильтром по имени или описанию (для админки, UI)
    @Query("""
        SELECT t FROM TaskType t
        WHERE (:search IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))
        ORDER BY t.sortOrder ASC
    """)
    Page<TaskType> searchByNameOrDescription(
            @Param("search") String search,
            Pageable pageable
    );
}
