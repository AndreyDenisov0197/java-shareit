package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.mapper.RequestWithItemsMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.request.controller.RequestController.HEADER;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerIT {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private RequestServiceImpl service;
    private final String json = "application/json";

    private User user;
    private Request request;
    private RequestDto requestDto;

    @BeforeEach
    public void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("userName")
                .email("email@mail.ru")
                .build();

        request = Request.builder()
                .id(2L)
                .description("text")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        requestDto = RequestMapper.toRequestDto(request);
    }


    @SneakyThrows
    @Test
    void createRequest() {
        when(service.createRequest(requestDto, user.getId())).thenReturn(requestDto);

        String result = mvc.perform(post("/requests")
                        .contentType(json)
                        .header(HEADER, user.getId())
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(requestDto), result);
    }


    @SneakyThrows
    @Test
    void createRequest_whenDescriptionNotBlank() {
        requestDto.setDescription("      ");
        when(service.createRequest(requestDto, user.getId())).thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .contentType(json)
                        .header(HEADER, user.getId())
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void getRequestsByUserId() {
        Collection<RequestWithItemsDto> requestCollection =
                List.of(RequestWithItemsMapper.toRequestWithItemsDto(request));
        when(service.getRequestsByUserId(user.getId())).thenReturn(requestCollection);

        String result = mvc.perform(get("/requests")
                        .contentType(json)
                        .header(HEADER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(requestCollection), result);
    }

    @SneakyThrows
    @Test
    void getAllRequest() {
        int from = 0;
        int size = 10;
        long userId = 1L;
        RequestWithItemsDto requestItemsDto = RequestWithItemsMapper.toRequestWithItemsDto(request);

        when(service.getAllRequest(userId, from, size)).thenReturn(List.of(requestItemsDto));

        String result = mvc.perform(get("/requests/all")
                        .header(HEADER, userId)
                        .contentType(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(requestItemsDto)), result);
    }

    @SneakyThrows
    @Test
    void getRequestById() {
        RequestWithItemsDto requestWithItemsDto = RequestWithItemsMapper.toRequestWithItemsDto(request);
        when(service.getRequestById(request.getId(), user.getId())).thenReturn(requestWithItemsDto);

        String result = mvc.perform(get("/requests/{requestId}", request.getId())
                        .header(HEADER, user.getId())
                        .contentType(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(requestWithItemsDto), result);
    }
}