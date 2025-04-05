package ru.otus.java.professional.yampolskiy.tttaskservice.tasks.services.filtering;

import org.springframework.data.jpa.domain.Specification;
import ru.otus.java.professional.yampolskiy.tttaskservice.tasks.entities.Task;

import java.time.Instant;
import java.util.UUID;

public class TaskSpecifications {

    public static Specification<Task> hasCreator(UUID creatorId) {
        return (root, query, cb) -> creatorId == null ? null : cb.equal(root.get("creatorId"), creatorId);
    }

    public static Specification<Task> hasAssignee(UUID assigneeId) {
        return (root, query, cb) -> assigneeId == null ? null : cb.equal(root.get("assigneeId"), assigneeId);
    }

    public static Specification<Task> hasStatus(String statusCode) {
        return (root, query, cb) -> (statusCode == null || statusCode.isBlank()) ? null :
                cb.equal(root.get("statusCode"), statusCode);
    }

    public static Specification<Task> hasType(String typeCode) {
        return (root, query, cb) -> (typeCode == null || typeCode.isBlank()) ? null :
                cb.equal(root.get("typeCode"), typeCode);
    }

    public static Specification<Task> createdAfter(Instant after) {
        return (root, query, cb) -> after == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), after);
    }

    public static Specification<Task> createdBefore(Instant before) {
        return (root, query, cb) -> before == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), before);
    }

    public static Specification<Task> isNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }
}
