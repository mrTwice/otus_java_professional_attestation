package ru.otus.java.professional.yampolskiy.tttaskservice.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignedUploadResponse {
    private String presignedUrl;
    private String fileUrl;
}
