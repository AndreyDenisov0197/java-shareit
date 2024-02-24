package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.model.Comment;

public class ItemCommentsMapper {

    public static ItemCommentsDto toItemComments(Comment comment) {

        return new ItemCommentsDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
