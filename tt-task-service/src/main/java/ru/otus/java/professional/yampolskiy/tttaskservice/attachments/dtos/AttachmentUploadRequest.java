package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AttachmentUploadRequest {
    @NotNull
    private UUID taskId;

    @NotBlank
    private String fileName;

    private Long fileSize;
    private String contentType;
}