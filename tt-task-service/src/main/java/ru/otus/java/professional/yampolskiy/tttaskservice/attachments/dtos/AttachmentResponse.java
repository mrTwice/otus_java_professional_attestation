package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
