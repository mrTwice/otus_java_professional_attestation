package ru.otus.java.professional.yampolskiy.tttaskservice.comments.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.entities.Comment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    // Получить все комментарии по задаче, отсортированные по дате создания
    List<Comment> findByTaskIdOrderByCreatedAtAsc(UUID taskId);

    // Получить все комментарии по автору
    List<Comment> findByAuthorId(UUID authorId);

    // Поиск комментариев по тексту (например, для админки)
    @Query("""
        SELECT c FROM Comment c
        WHERE (:taskId IS NULL OR c.taskId = :taskId)
          AND (:authorId IS NULL OR c.authorId = :authorId)
          AND (:text IS NULL OR LOWER(c.content) LIKE LOWER(CONCAT('%', :text, '%')))
    """)
    Page<Comment> searchComments(
            @Param("taskId") UUID taskId,
            @Param("authorId") UUID authorId,
            @Param("text") String text,
            Pageable pageable
    );

    // Подсчёт количества комментариев по задаче
    long countByTaskId(UUID taskId);

    // Комментарии, созданные за период (например, активность)
    List<Comment> findByCreatedAtBetween(Instant from, Instant to);
}
