package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceDb;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.controller.ItemController.HEADER;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerIT {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemServiceDb itemService;
    private final String json = "application/json";

    @SneakyThrows
    @Test
    void createItem() {
        long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("qwerty");
        itemDto.setAvailable(true);
        when(itemService.createItem(userId, itemDto)).thenReturn(itemDto);

        String result = mvc.perform(post("/items")
                        .contentType(json)
                        .header(HEADER, userId)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void createItem_whenItemNotName() {
        long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setDescription("qwerty");
        itemDto.setAvailable(true);
        when(itemService.createItem(userId, itemDto)).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header(HEADER, userId)
                        .contentType(json)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).createItem(userId, itemDto);
    }

    @SneakyThrows
    @Test
    void createItem_whenUserIdNotPositive() {
        long userId = -10L;
        ItemDto itemDto = new ItemDto();
        itemDto.setDescription("qwerty");
        itemDto.setAvailable(true);
        when(itemService.createItem(userId, itemDto)).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header(HEADER, userId)
                        .contentType(json)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).createItem(userId, itemDto);
    }

    @SneakyThrows
    @Test
    void updateItem() {
        long itemId = 2L;
        long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("qwerty");
        itemDto.setAvailable(true);

        ItemDto updateItem = new ItemDto(2L, "name", "qwerty", true, null);
        when(itemService.updateItem(userId, itemDto, itemId)).thenReturn(updateItem);

        String result = mvc.perform(patch("/items/{itemId}", itemId)
                        .header(HEADER, userId)
                        .contentType(json)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(updateItem), result);
    }

    @SneakyThrows
    @Test
    void updateItem_whenUserIdFromHeaderNotPositive() {
        long itemId = 2L;
        long userId = -1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("qwerty");
        itemDto.setAvailable(true);

        ItemDto updateItem = new ItemDto(2L, "name", "qwerty", true, null);
        when(itemService.updateItem(userId, itemDto, itemId)).thenReturn(updateItem);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .header(HEADER, userId)
                        .contentType(json)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void updateItem_whenItemIdFromHeaderNotPositive() {
        long itemId = -2L;
        long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("qwerty");
        itemDto.setAvailable(true);

        ItemDto updateItem = new ItemDto(2L, "name", "qwerty", true, null);
        when(itemService.updateItem(userId, itemDto, itemId)).thenReturn(updateItem);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .header(HEADER, userId)
                        .contentType(json)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void update_whenItemDescription() {
        long userId = 1L;
        long itemId = 2L;
        ItemDto itemDto = new ItemDto();
        itemDto.setDescription("Description");

        ItemDto updateItem = new ItemDto(2L, "name", "Description", true, null);
        when(itemService.updateItem(userId, itemDto, itemId)).thenReturn(updateItem);

        String result = mvc.perform(patch("/items/{itemId}", itemId)
                        .header(HEADER, userId)
                        .contentType(json)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(updateItem), result);
    }

    @SneakyThrows
    @Test
    void getItemById_whenUserIdNotPositive() {
        long userId = -2L;
        long itemId = 1L;
        mvc.perform(get("/items/{itemId}", itemId)
                        .header(HEADER, userId))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).getItemById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void getItemById() {
        long userId = 2L;
        long itemId = 1L;
        mvc.perform(get("/items/{itemId}", itemId)
                        .header(HEADER, userId))
                .andExpect(status().isOk());

        verify(itemService).getItemById(itemId, userId);
    }

    @SneakyThrows
    @Test
    void getItemsByUser() {
        int from = 0;
        int size = 20;
        long userId = 2L;
        mvc.perform(get("/items")
                        .header(HEADER, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(itemService).getItemsByUser(userId, from, size);
    }

    @SneakyThrows
    @Test
    void searchItem() {
        String text = "text";
        int from = 0;
        int size = 20;

        mvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(itemService).searchItem(text, from, size);
    }

    @SneakyThrows
    @Test
    void createComments() {
        long userId = 2L;
        long itemId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("text");

        ItemCommentsDto itemDto = new ItemCommentsDto(1L, "text", "userName", LocalDateTime.now());
        when(itemService.createComments(itemId, userId, commentDto)).thenReturn(itemDto);

        String result = mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(HEADER, userId)
                        .contentType(json)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).createComments(itemId, userId, commentDto);
        assertEquals(mapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void createComments_whenTextIsBlank() {
        long userId = 2L;
        long itemId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("    ");

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(HEADER, userId)
                        .contentType(json)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());
    }
}