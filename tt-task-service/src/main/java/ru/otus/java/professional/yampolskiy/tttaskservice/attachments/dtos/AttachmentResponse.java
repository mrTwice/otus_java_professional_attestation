package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import java.time.Instant;
import java.util.UUID;

public class AttachmentResponse {
    private UUID id;
    private UUID taskId;

    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String contentType;

    private UUID uploadedBy;
    private Instant uploadedAt;
}
