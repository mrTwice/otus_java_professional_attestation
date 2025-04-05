package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import jakarta.validation.constraints.NotBlank;

public class AttachmentUpdateRequest {
    @NotBlank
    private String fileName;
}
