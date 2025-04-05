package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.entities.Attachment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    // Все вложения по задаче
    List<Attachment> findByTaskId(UUID taskId);

    // Удалить все вложения задачи (например, при удалении задачи)
    void deleteByTaskId(UUID taskId);

    // Вложения пользователя
    List<Attachment> findByUploadedBy(UUID uploadedBy);

    // Поиск по имени файла
    @Query("""
        SELECT a FROM Attachment a
        WHERE (:taskId IS NULL OR a.taskId = :taskId)
          AND (:uploadedBy IS NULL OR a.uploadedBy = :uploadedBy)
          AND (:fileName IS NULL OR LOWER(a.fileName) LIKE LOWER(CONCAT('%', :fileName, '%')))
    """)
    Page<Attachment> search(
            @Param("taskId") UUID taskId,
            @Param("uploadedBy") UUID uploadedBy,
            @Param("fileName") String fileName,
            Pageable pageable
    );

    // Подсчёт вложений по задаче
    long countByTaskId(UUID taskId);

    // Подсчёт общего размера вложений по задаче
    @Query("SELECT SUM(a.fileSize) FROM Attachment a WHERE a.taskId = :taskId")
    Long sumFileSizeByTaskId(@Param("taskId") UUID taskId);

    // Вложения, загруженные в определённый период (например, лог аудита)
    List<Attachment> findByUploadedAtBetween(Instant from, Instant to);
}
