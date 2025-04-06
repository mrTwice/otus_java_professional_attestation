package ru.otus.java.professional.yampolskiy.tttaskservice.attachments.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos.AttachmentResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos.AttachmentUpdateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.dtos.AttachmentUploadRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.attachments.entities.Attachment;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AttachmentMapper {

    Attachment toEntity(AttachmentUploadRequest request);

    void updateEntity(@MappingTarget Attachment entity, AttachmentUpdateRequest update);

    AttachmentResponse toResponse(Attachment attachment);

    List<AttachmentResponse> toResponseList(List<Attachment> attachments);
}
