package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentUpdateRequest {
    @NotBlank
    private String fileName;
}
