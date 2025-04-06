package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos.AttachmentDeleteRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos.AttachmentResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos.AttachmentUpdateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos.AttachmentUploadRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.entities.Attachment;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.mappers.AttachmentMapper;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.services.AttachmentService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
@Validated
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final AttachmentMapper attachmentMapper;

    // Загрузка метаинформации после загрузки файла
    @PostMapping
    @PreAuthorize("@attachmentAccessPolicy.canCreateAttachment(authentication)")
    public AttachmentResponse upload(@RequestBody @Valid AttachmentUploadRequest request,
                                     Authentication authentication) {
        Attachment attachment = attachmentMapper.toEntity(request);
        attachment.setUploadedBy(getUserIdFromAuth(authentication));
        Attachment saved = attachmentService.upload(attachment);
        return attachmentMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@attachmentAccessPolicy.canUpdateAttachment(authentication)")
    public AttachmentResponse update(@PathVariable UUID id,
                                     @RequestBody @Valid AttachmentUpdateRequest request) {
        Attachment existing = attachmentService.findById(id);
        attachmentMapper.updateEntity(existing, request);
        Attachment updated = attachmentService.update(id, existing);
        return attachmentMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@attachmentAccessPolicy.canDeleteAttachment(authentication)")
    public void delete(@PathVariable UUID id) {
        attachmentService.delete(id);
    }

    @PostMapping("/delete")
    @PreAuthorize("@attachmentAccessPolicy.canDeleteAttachment(authentication)")
    public void deleteMany(@RequestBody @Valid AttachmentDeleteRequest request) {
        attachmentService.deleteMany(request.getAttachmentIds());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public AttachmentResponse getById(@PathVariable UUID id) {
        return attachmentMapper.toResponse(attachmentService.findById(id));
    }

    @GetMapping("/by-task/{taskId}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public List<AttachmentResponse> getByTask(@PathVariable UUID taskId) {
        return attachmentMapper.toResponseList(attachmentService.findByTaskId(taskId));
    }

    @GetMapping("/by-uploader/{userId}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachmentsOfUser(authentication, #userId)")
    public List<AttachmentResponse> getByUploader(@PathVariable UUID userId) {
        return attachmentMapper.toResponseList(attachmentService.findByUploader(userId));
    }

    @GetMapping("/count/by-task/{taskId}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public long countByTask(@PathVariable UUID taskId) {
        return attachmentService.countByTaskId(taskId);
    }

    @GetMapping("/size/by-task/{taskId}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public Long getTotalSize(@PathVariable UUID taskId) {
        return attachmentService.getTotalSizeByTask(taskId);
    }

    @GetMapping("/search")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public Page<AttachmentResponse> search(@RequestParam(required = false) UUID taskId,
                                           @RequestParam(required = false) UUID uploadedBy,
                                           @RequestParam(required = false) String fileName,
                                           Pageable pageable) {
        return attachmentService.search(taskId, uploadedBy, fileName, pageable)
                .map(attachmentMapper::toResponse);
    }

    private UUID getUserIdFromAuth(Authentication authentication) {
        Jwt jwt = (authentication instanceof JwtAuthenticationToken jwtToken)
                ? jwtToken.getToken()
                : (authentication.getPrincipal() instanceof Jwt j ? j : null);

        if (jwt == null) throw new AccessDeniedException("No valid JWT found");
        return UUID.fromString(jwt.getSubject());
    }
}
