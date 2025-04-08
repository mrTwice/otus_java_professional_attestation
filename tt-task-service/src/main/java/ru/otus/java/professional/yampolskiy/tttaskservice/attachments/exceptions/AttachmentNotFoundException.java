package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.exceptions;

import java.util.UUID;

public class AttachmentNotFoundException extends AttachmentException {
  public AttachmentNotFoundException(UUID id) {
    super("Attachment not found with id: " + id);
  }
}
