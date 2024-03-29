package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(CommentDto comment) {
        return new Comment(
                comment.getId(),
                comment.getText(),
                null,
                null,
                LocalDateTime.now()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText()
        );
    }

    public static ItemCommentsDto toItemComments(Comment comment) {
        return new ItemCommentsDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
