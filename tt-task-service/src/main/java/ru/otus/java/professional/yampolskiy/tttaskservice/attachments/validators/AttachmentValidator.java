package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.validators;

import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.entities.Attachment;
import ru.otus.java.professional.yampolskiy.tttaskservice.common.DomainValidator;

public class AttachmentValidator implements DomainValidator<Attachment> {

    @Override
    public void validateForCreate(Attachment attachment) {
        validateCommon(attachment);
    }

    @Override
    public void validateForUpdate(Attachment existing, Attachment updated) {
        if (!existing.getTaskId().equals(updated.getTaskId())) {
            throw new IllegalArgumentException("Task ID cannot be changed");
        }
        if (!existing.getUploadedBy().equals(updated.getUploadedBy())) {
            throw new IllegalArgumentException("Uploader cannot be changed");
        }
        validateCommon(updated);
    }

    private void validateCommon(Attachment attachment) {
        if (attachment.getFileName() == null || attachment.getFileName().isBlank()) {
            throw new IllegalArgumentException("File name is required");
        }
        if (attachment.getFileSize() != null && attachment.getFileSize() > 100_000_000) { // например, 100MB
            throw new IllegalArgumentException("File too large");
        }
    }
}
