package ru.otus.java.professional.yampolskiy.tttaskservice.comments.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos.CommentCreateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos.CommentResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos.CommentUpdateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.entities.Comment;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CommentMapper {

    Comment toEntity(CommentCreateRequest request);

    void updateEntity(@MappingTarget Comment target, CommentUpdateRequest update);

    CommentResponse toResponse(Comment entity);

    List<CommentResponse> toResponseList(List<Comment> comments);
}
