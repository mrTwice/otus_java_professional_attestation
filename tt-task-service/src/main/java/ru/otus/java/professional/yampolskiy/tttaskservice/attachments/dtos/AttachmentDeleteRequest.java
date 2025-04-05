package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDeleteRequest {
    @NotEmpty
    private List<UUID> attachmentIds;
}
