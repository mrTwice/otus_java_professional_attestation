package ru.otus.java.professional.yampolskiy.tttaskservice.comments.controllers;

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
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos.CommentCreateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos.CommentListResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos.CommentResponse;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.dtos.CommentUpdateRequest;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.entities.Comment;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.mappers.CommentMapper;
import ru.otus.java.professional.yampolskiy.tttaskservice.comments.services.CommentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    @PreAuthorize("@commentAccessPolicy.canCreateComment(authentication)")
    public CommentResponse create(@RequestBody @Valid CommentCreateRequest request,
                                  Authentication authentication) {
        Comment comment = commentMapper.toEntity(request);
        comment.setAuthorId(getUserIdFromAuth(authentication));
        Comment created = commentService.create(comment);
        return commentMapper.toResponse(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@commentAccessPolicy.canEditOwnCommentOnly(authentication, @commentService.findById(#id).authorId)")
    public CommentResponse update(@PathVariable UUID id,
                                  @RequestBody @Valid CommentUpdateRequest request) {
        Comment existing = commentService.findById(id);
        commentMapper.updateEntity(existing, request);
        Comment updated = commentService.update(id, existing);
        return commentMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@commentAccessPolicy.canManageCommentOfUser(authentication, @commentService.findById(#id).authorId)")
    public void delete(@PathVariable UUID id) {
        commentService.delete(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@commentAccessPolicy.canViewComment(authentication)")
    public CommentResponse getById(@PathVariable UUID id) {
        return commentMapper.toResponse(commentService.findById(id));
    }

    @GetMapping("/by-task/{taskId}")
    @PreAuthorize("@commentAccessPolicy.canViewComment(authentication)")
    public CommentListResponse getByTask(@PathVariable UUID taskId) {
        List<Comment> comments = commentService.findByTaskId(taskId);
        List<CommentResponse> responseList = commentMapper.toResponseList(comments);
        return new CommentListResponse(taskId, responseList);
    }

    @GetMapping("/search")
    @PreAuthorize("@commentAccessPolicy.canViewComment(authentication)")
    public Page<CommentResponse> search(@RequestParam(required = false) UUID taskId,
                                        @RequestParam(required = false) UUID authorId,
                                        @RequestParam(required = false) String text,
                                        Pageable pageable) {
        return commentService.search(taskId, authorId, text, pageable)
                .map(commentMapper::toResponse);
    }

    @GetMapping("/count/by-task/{taskId}")
    @PreAuthorize("@commentAccessPolicy.canViewComment(authentication)")
    public long countByTask(@PathVariable UUID taskId) {
        return commentService.countByTaskId(taskId);
    }

    private UUID getUserIdFromAuth(Authentication authentication) {
        Jwt jwt = (authentication instanceof JwtAuthenticationToken jwtToken)
                ? jwtToken.getToken()
                : (authentication.getPrincipal() instanceof Jwt j ? j : null);

        if (jwt == null) throw new AccessDeniedException("No valid JWT found");
        return UUID.fromString(jwt.getSubject());
    }
}