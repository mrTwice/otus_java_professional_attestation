package ru.otus.java.professional.yampolskiy.tttaskservice.comments.exceptions;

public class CommentAccessDeniedException extends CommentException {
    public CommentAccessDeniedException() {
        super("Access to the comment is denied.");
    }
}
