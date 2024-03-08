package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {
    private User user;
    private Item item;
    private ItemDto itemDto;
    private Comment comment;
    private CommentDto commentDto;
    private LocalDateTime date;

    @BeforeEach
    public void beforeEach() {
        date = LocalDateTime.now();

        user = User.builder()
                .id(1L)
                .name("userName")
                .email("email@mail.ru")
                .build();

        item = Item.builder()
                .id(2L)
                .owner(user)
                .name("nameItem")
                .description("qwe")
                .available(true)
                .build();

        itemDto = ItemDto.builder()
                .id(2L)
                .name("nameItem")
                .description("qwe")
                .available(true)
                .requestId(null)
                .build();

        comment = Comment.builder()
                .id(5L)
                .text("text")
                .created(date)
                .build();

        commentDto = CommentDto.builder()
                .id(5L)
                .text("text")
                .build();
    }

        @Test
    void toItemDto() {
        ItemDto result = ItemMapper.toItemDto(item);

        assertEquals(itemDto, result);
    }

    @Test
    void toItem() {
        item.setId(null);
        item.setOwner(null);
        Item result = ItemMapper.toItem(itemDto);

        assertEquals(item, result);
    }

    @Test
    void toOutgoingItemCollection() {
        OutgoingItemDto outgoingItemDto = new OutgoingItemDto(2L, "nameItem", "qwe",
                true, null, null, null);
        OutgoingItemDto result = OutgoingItemMapper.toOutgoingItemCollection(item);

        assertEquals(outgoingItemDto, result);
    }

    @Test
    void toComment() {
        Comment result = CommentMapper.toComment(commentDto);
        result.setCreated(date);

        assertEquals(comment, result);
    }

    @Test
    void toCommentDto() {
        CommentDto result = CommentMapper.toCommentDto(comment);

        assertEquals(commentDto, result);
    }

    @Test
    void toItemComments() {
        comment.setAuthor(user);
        ItemCommentsDto itemCommentsDto = new ItemCommentsDto(5L, "text", user.getName(), date);
        ItemCommentsDto result = ItemCommentsMapper.toItemComments(comment);

        assertEquals(itemCommentsDto, result);
    }
}