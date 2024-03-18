package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OutgoingItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;

    @Test
    void createItem() {
        ItemDto item = new ItemDto();
        long userId = 1L;
        when(itemService.createItem(userId, item)).thenReturn(item);

        ResponseEntity<ItemDto> response = itemController.createItem(userId, item);

        assertEquals(item, response.getBody());
        verify(itemService).createItem(userId, item);
    }

    @Test
    void updateItem() {
        ItemDto itemDto = new ItemDto();
        long userId = 1L;
        long itemId = 0L;
        Mockito.when(itemService.updateItem(userId, itemDto, itemId)).thenReturn(itemDto);

        ResponseEntity<ItemDto> response = itemController.updateItem(userId, itemDto, itemId);

        assertEquals(itemDto, response.getBody());
        verify(itemService).updateItem(userId, itemDto, itemId);
    }

    @Test
    void getItemById() {
        OutgoingItemDto item = new OutgoingItemDto();
        long userId = 1L;
        long itemId = 0L;
        when(itemService.getItemById(itemId, userId)).thenReturn(item);

        ResponseEntity<OutgoingItemDto> response = itemController.getItemById(itemId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
    }

    @Test
    void getItemsByUser() {
        Collection<OutgoingItemDto> items = List.of(new OutgoingItemDto());
        long userId = 1L;
        int from = 0;
        int size = 20;
        when(itemService.getItemsByUser(userId, from, size)).thenReturn(items);

        ResponseEntity<Collection<OutgoingItemDto>> response = itemController.getItemsByUser(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
    }

    @Test
    void searchItem() {
        int size = 10;
        int from = 0;
        String text = "text";
        Collection<ItemDto> items = List.of(new ItemDto());
        when(itemService.searchItem(text, from, size)).thenReturn(items);

        ResponseEntity<Collection<ItemDto>> response = itemController.searchItem(text, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
    }

    @Test
    void createComments() {
        CommentDto comment = new CommentDto();
        long userId = 5L;
        long itemId = 1L;
        ItemCommentsDto itemCommentsDto = new ItemCommentsDto();
        when(itemService.createComments(itemId, userId, comment)).thenReturn(itemCommentsDto);

        ResponseEntity<ItemCommentsDto> response = itemController.createComments(itemId, userId, comment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemCommentsDto, response.getBody());
    }
}