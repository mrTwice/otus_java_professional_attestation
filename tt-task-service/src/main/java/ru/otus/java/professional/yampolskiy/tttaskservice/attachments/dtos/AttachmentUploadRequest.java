package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentUploadRequest {
    @NotNull
    private UUID taskId;

    @NotBlank
    private String fileName;

    private Long fileSize;
    private String contentType;
}