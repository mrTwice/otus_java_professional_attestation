package ru.otus.java.professional.yampolskiy.tttaskservice.storage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.tttaskservice.storage.dto.PresignedUploadResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.storage.service.FileStorageClient;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
@Validated
public class FileStorageController {

    private final FileStorageClient fileStorageClient;

    @GetMapping("/upload-url")
    @PreAuthorize("@storageAccessPolicy.hasPermission(authentication, 'attachment:create')")
    public PresignedUploadResponse getPresignedUploadUrl(@RequestParam String fileName) {
        return fileStorageClient.generateUploadUrl(fileName);
    }
}

