package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {
    @Mock
    private RequestService requestService;
    @InjectMocks
    private RequestController requestController;
    private User user;
    private Request request;
    private RequestDto requestDto;
    private RequestDto requestUpdate;

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

        requestUpdate = RequestDto.builder()
                .description("text")
                .build();
    }

    @Test
    void createRequest() {
        when(requestService.createRequest(requestUpdate, user.getId())).thenReturn(requestDto);

        ResponseEntity<RequestDto> response = requestController.createRequest(requestUpdate, user.getId());

        assertEquals(requestDto, response.getBody());
        verify(requestService).createRequest(requestUpdate, user.getId());
    }

    @Test
    void getRequestsByUserId() {
        Collection<RequestWithItemsDto> requestCollection =
                List.of(RequestMapper.toRequestWithItemsDto(request));
        when(requestService.getRequestsByUserId(user.getId())).thenReturn(requestCollection);

        ResponseEntity<Collection<RequestWithItemsDto>> response = requestController.getRequestsByUserId(user.getId());

        assertEquals(requestCollection, response.getBody());
        verify(requestService).getRequestsByUserId(user.getId());
        assertEquals(requestCollection.size(), response.getBody().size());
    }

    @Test
    void getAllRequest() {
        int from = 0;
        int size = 20;
        Collection<RequestWithItemsDto> requestCollection =
                List.of(RequestMapper.toRequestWithItemsDto(request));
        when(requestService.getAllRequest(user.getId(), from, size)).thenReturn(requestCollection);

        ResponseEntity<Collection<RequestWithItemsDto>> response =
                requestController.getAllRequest(user.getId(), from, size);

        assertEquals(requestCollection, response.getBody());
        verify(requestService).getAllRequest(user.getId(), from, size);
        assertEquals(requestCollection.size(), response.getBody().size());
    }

    @Test
    void getRequestById() {
        RequestWithItemsDto requestWithItemsDto = RequestMapper.toRequestWithItemsDto(request);
        when(requestService.getRequestById(request.getId(), user.getId())).thenReturn(requestWithItemsDto);

        ResponseEntity<RequestWithItemsDto> response = requestController.getRequestById(request.getId(), user.getId());

        assertEquals(requestWithItemsDto, response.getBody());
        verify(requestService).getRequestById(request.getId(), user.getId());
    }
}