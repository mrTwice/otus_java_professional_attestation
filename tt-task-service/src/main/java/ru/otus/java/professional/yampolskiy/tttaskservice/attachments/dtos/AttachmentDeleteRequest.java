package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public class AttachmentDeleteRequest {
    @NotEmpty
    private List<UUID> attachmentIds;
}
