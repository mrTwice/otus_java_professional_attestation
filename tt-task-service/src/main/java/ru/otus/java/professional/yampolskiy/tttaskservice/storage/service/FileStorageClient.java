package ru.otus.java.professional.yampolskiy.tttaskservice.storage.service;

import ru.otus.java.professional.yampolskiy.tttaskservice.storage.dto.PresignedUploadResponse;

public interface FileStorageClient {
    PresignedUploadResponse generateUploadUrl(String fileName);
}
