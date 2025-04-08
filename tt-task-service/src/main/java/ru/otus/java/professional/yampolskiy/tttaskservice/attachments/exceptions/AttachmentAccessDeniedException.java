package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.exceptions;

public class AttachmentAccessDeniedException extends AttachmentException {
    public AttachmentAccessDeniedException() {
        super("Access denied to attachment resource.");
    }
}
